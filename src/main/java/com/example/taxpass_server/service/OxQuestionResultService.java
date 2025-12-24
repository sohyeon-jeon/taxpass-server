package com.example.taxpass_server.service;

import com.example.taxpass_server.dto.SaveOxAnswerRequest;
import com.example.taxpass_server.entity.OxQuestion;
import com.example.taxpass_server.entity.OxQuestionResult;
import com.example.taxpass_server.entity.User;
import com.example.taxpass_server.repository.OxQuestionRepository;
import com.example.taxpass_server.repository.OxQuestionResultRepository;
import com.example.taxpass_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OxQuestionResultService {

    private final OxQuestionResultRepository oxQuestionResultRepository;
    private final UserRepository userRepository;
    private final OxQuestionRepository oxQuestionRepository;

    @Transactional
    public void saveResult(SaveOxAnswerRequest request, Long userKakaoId) {

        // 1. 사용자 조회 (kakaoId 기준)
        User user = userRepository.findByKakaoId(userKakaoId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        // 2. 문제 조회 (ox_questions.id 기준)
        OxQuestion question = oxQuestionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new IllegalArgumentException("문제 없음"));

        // 3. 기존 결과 있으면 가져오고, 없으면 새로 생성
        OxQuestionResult result = oxQuestionResultRepository
                .findByUserAndOxQuestion(user, question)
                .orElseGet(() ->
                        OxQuestionResult.builder()
                                .user(user)
                                .oxQuestion(question)
                                .build()
                );

        // 4. 답안 갱신
        result.updateAnswer(
                request.isUserAnswer(),
                request.wasCorrect()
        );

        result.touch();

        // 5. 저장 (INSERT or UPDATE 자동 판단)
        oxQuestionResultRepository.save(result);
    }
}
