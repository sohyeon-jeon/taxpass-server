package com.example.taxpass_server.controller;

import com.example.taxpass_server.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionRepository questionRepository;

    @GetMapping("/detail")
    public List<Object[]> getRawQuestionsWithChoices() {
        return questionRepository.findQuestionBundle();
    }
}