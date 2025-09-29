package com.example.taxpass_server.controller;

import com.example.taxpass_server.service.OxQuestionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ox-questions")
public class OxQuestionController {

    private final OxQuestionService oxQuestionService;

    @GetMapping("/{subjectId}")
    public List<Object[]> getOxQuestionsBySubjectId(@PathVariable Long subjectId, HttpServletRequest request) {
        Long kakaoId = (Long) request.getAttribute("kakaoId");
        return oxQuestionService.getOxQuestionsBySubjectId(subjectId, kakaoId);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadOxQuestions(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please select a file to upload.");
        }

        try {
            Object kakaoIdAttr = request.getAttribute("kakaoId");
            String kakaoId = (kakaoIdAttr != null) ? String.valueOf(kakaoIdAttr) : null;
            String remoteAddr = request.getHeader("X-Forwarded-For");
            if (remoteAddr == null || remoteAddr.isEmpty()) {
                remoteAddr = request.getRemoteAddr();
            }
            oxQuestionService.uploadOxQuestions(file, kakaoId, remoteAddr);
            return ResponseEntity.status(HttpStatus.CREATED).body("Successfully uploaded and processed file: " + file.getOriginalFilename());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}