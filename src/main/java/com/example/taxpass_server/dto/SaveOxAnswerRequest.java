package com.example.taxpass_server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class SaveOxAnswerRequest {
    @Getter
    @JsonProperty("subjectId")
    private int subjectId;

    @Getter
    private String number;

    @Getter
    @JsonProperty("userAnswer")
    private boolean userAnswer;

    @JsonProperty("isCorrect")
    private boolean correct;

    public boolean wasCorrect() {
        return this.correct;
    }
}
