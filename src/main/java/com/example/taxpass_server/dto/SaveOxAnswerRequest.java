package com.example.taxpass_server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class SaveOxAnswerRequest {

    @JsonProperty("questionId")
    private Long questionId;

    @JsonProperty("userAnswer")
    private boolean userAnswer;

    @JsonProperty("isCorrect")
    private boolean correct;

    public boolean wasCorrect() {
        return this.correct;
    }
}
