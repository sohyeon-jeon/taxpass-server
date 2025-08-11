package com.example.taxpass_server.controller;

import com.example.taxpass_server.repository.OxQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ox-questions")
public class OxQuestionController {

    private final OxQuestionRepository oxQuestionRepository;

    @GetMapping("/{subjectId}")
    public List<Object[]> getOxQuestionsBySubjectId(@PathVariable Long subjectId) {
        return oxQuestionRepository.findOxQuestionsBySubjectId(subjectId);
    }
}
