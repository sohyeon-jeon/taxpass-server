package com.example.taxpass_server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "answers")
@Getter @Setter
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String correctAnswer;

    @Column(columnDefinition = "text")
    private String explanation;

    @OneToOne
    @JoinColumn(name = "question_id")
    private Question question;
}
