package com.example.taxpass_server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class OxQuestionId implements Serializable {

    @Column(name = "subject_id")
    private Long subjectId;

    @Column(name = "number", length = 10)
    private String number;

}
