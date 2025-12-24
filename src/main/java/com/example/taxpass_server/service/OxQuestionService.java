package com.example.taxpass_server.service;

import com.example.taxpass_server.entity.OxQuestion;
import com.example.taxpass_server.entity.User;
import com.example.taxpass_server.repository.OxQuestionRepository;
import com.example.taxpass_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OxQuestionService {

    private final OxQuestionRepository oxQuestionRepository;
    private final UserRepository userRepository;
    private final JdbcTemplate jdbcTemplate;

    public List<Object[]> getOxQuestionsBySubjectId(Long subjectId, Long kakaoId) {

        // kakaoId → User PK 변환
        User user = userRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

//        System.out.printf(
//                "[OX] subjectId=%d, kakaoId=%d, userId=%d%n",
//                subjectId,
//                kakaoId,
//                user.getId()
//        );

        return oxQuestionRepository.findOxQuestionsBySubjectId(
                subjectId,
                user.getId()
        );
    }


    @Transactional
    public void uploadOxQuestions(MultipartFile file, String userId, String remoteAddr) throws IOException {
        String extension = getFileExtension(file.getOriginalFilename());
        List<OxQuestion> questions;

        if ("xlsx".equalsIgnoreCase(extension)) {
            questions = parseXlsx(file, userId, remoteAddr);
        } else if ("csv".equalsIgnoreCase(extension)) {
            questions = parseCsv(file, CSVFormat.DEFAULT, userId, remoteAddr);
        } else if ("tsv".equalsIgnoreCase(extension)) {
            questions = parseCsv(file, CSVFormat.TDF, userId, remoteAddr);
        } else {
            throw new IllegalArgumentException("Unsupported file format: " + extension);
        }

        saveWithNativeQuery(questions);
    }

    private void saveWithNativeQuery(List<OxQuestion> questions) {
        String sql = "INSERT INTO ox_questions (subject_id, number, question_text, answer, explanation, created_at, updated_at, created_by_ip, created_by_user_id, updated_by_ip, updated_by_user_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, CAST(? AS inet), ?, CAST(? AS inet), ?)";

//        성능 향상을 위해 JdbcTemplate을 사용한 네이티브
//        쿼리로 대량 삽입(bulk insert)을 수행
        jdbcTemplate.execute(sql, (PreparedStatement ps) -> {
            // --- 진단 코드 시작 ---
            try {
                System.out.println("--- JDBC 파라미터 타입 진단 시작 ---");
                java.sql.ParameterMetaData pmd = ps.getParameterMetaData();
                int paramCount = pmd.getParameterCount();
                System.out.println("파라미터 개수: " + paramCount);
                for (int i = 1; i <= paramCount; i++) {
                    System.out.println("파라미터 #" + i + ": " + pmd.getParameterTypeName(i) + " (JDBC Type: " + pmd.getParameterType(i) + ")");
                }
                System.out.println("--- JDBC 파라미터 타입 진단 종료 ---");
            } catch (Exception e) {
                System.out.println("진단 코드 실행 중 오류 발생: " + e.getMessage());
            }
            // --- 진단 코드 종료 ---

            for (OxQuestion question : questions) {
                ps.setLong(1, question.getSubjectId());
                ps.setString(2, question.getNumber());
                ps.setString(3, question.getQuestionText());
                ps.setBoolean(4, question.isAnswer());
                ps.setString(5, question.getExplanation());
                ps.setObject(6, question.getCreatedAt());
                ps.setObject(7, question.getUpdatedAt());
                ps.setString(8, question.getCreatedByIp());
                ps.setString(9, question.getCreatedByUserId());
                ps.setString(10, question.getUpdatedByIp());
                ps.setString(11, question.getUpdatedByUserId());
                ps.addBatch();
            }
            return ps.executeBatch();
        });
    }

    private List<OxQuestion> parseXlsx(MultipartFile file, String userId, String remoteAddr) throws IOException {
        List<OxQuestion> questions = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) { // Skip header
                    continue;
                }
                questions.add(createQuestionFromRow(row, userId, remoteAddr));
            }
        }
        return questions;
    }

    private List<OxQuestion> parseCsv(MultipartFile file, CSVFormat format, String userId, String remoteAddr) throws IOException {
        List<OxQuestion> questions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(reader, format.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            for (CSVRecord csvRecord : csvParser) {
                questions.add(createQuestionFromCsvRecord(csvRecord, userId, remoteAddr));
            }
        }
        return questions;
    }

    private OxQuestion createQuestionFromRow(Row row, String userId, String remoteAddr) {
        OxQuestion question = new OxQuestion();
        question.setSubjectId((long) row.getCell(0).getNumericCellValue());
        question.setNumber(String.valueOf((int) row.getCell(1).getNumericCellValue()));
        question.setQuestionText(row.getCell(2).getStringCellValue());
        question.setAnswer(Boolean.parseBoolean(row.getCell(3).getStringCellValue()));
        question.setExplanation(row.getCell(4).getStringCellValue());
        question.setCreatedAt(LocalDateTime.now());
        question.setUpdatedAt(LocalDateTime.now());
        question.setCreatedByIp(remoteAddr);
        question.setCreatedByUserId(userId);
        question.setUpdatedByIp(remoteAddr);
        question.setUpdatedByUserId(userId);
        return question;
    }

    private OxQuestion createQuestionFromCsvRecord(CSVRecord record, String userId, String remoteAddr) {
        OxQuestion question = new OxQuestion();
        question.setSubjectId(Long.parseLong(record.get("subject_id")));
        question.setNumber(record.get("number"));
        question.setQuestionText(record.get("question_text"));
        question.setAnswer(Boolean.parseBoolean(record.get("answer")));
        question.setExplanation(record.get("explanation"));
        question.setCreatedAt(LocalDateTime.now());
        question.setUpdatedAt(LocalDateTime.now());
        question.setCreatedByIp(remoteAddr);
        question.setCreatedByUserId(userId);
        question.setUpdatedByIp(remoteAddr);
        question.setUpdatedByUserId(userId);
        return question;
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }
}