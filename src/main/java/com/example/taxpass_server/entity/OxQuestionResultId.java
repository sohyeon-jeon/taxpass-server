package com.example.taxpass_server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OxQuestionResultId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "subject_id")
    private Integer subjectId;

    @Column(name = "number")
    private String number;
}
