package com.example.taxpass_server.controller;

import com.example.taxpass_server.dto.SaveOxAnswerRequest;
import com.example.taxpass_server.service.OxQuestionResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OxQuestionResultController {

    private final OxQuestionResultService oxQuestionResultService;

    @PostMapping("/ox-answers")
    public ResponseEntity<Void> saveOxAnswer(
            @RequestBody SaveOxAnswerRequest request,
            @RequestAttribute("kakaoId") Long kakaoId) {


        oxQuestionResultService.saveResult(request, kakaoId);
        return ResponseEntity.ok().build();
    }

}
