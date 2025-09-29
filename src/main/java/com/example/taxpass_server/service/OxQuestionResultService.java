package com.example.taxpass_server.service;

import com.example.taxpass_server.dto.SaveOxAnswerRequest;
import com.example.taxpass_server.entity.OxQuestionResult;
import com.example.taxpass_server.entity.OxQuestionResultId;
import com.example.taxpass_server.repository.OxQuestionResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OxQuestionResultService {

    private final OxQuestionResultRepository oxQuestionResultRepository;

    @Transactional
    public void saveResult(SaveOxAnswerRequest request, Long userKakaoId) {
        OxQuestionResultId resultId = new OxQuestionResultId(
                userKakaoId,
                request.getSubjectId(),
                request.getNumber()
        );

        OxQuestionResult result = OxQuestionResult.builder()
                .id(resultId)
                .userAnswer(request.isUserAnswer())
                .isCorrect(request.wasCorrect())
                .build();

        oxQuestionResultRepository.save(result);
    }
}
