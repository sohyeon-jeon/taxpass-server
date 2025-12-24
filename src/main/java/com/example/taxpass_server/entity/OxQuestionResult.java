package com.example.taxpass_server.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
@Getter
@Entity
@Table(
        name = "ox_question_results",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_user_question",
                        columnNames = {"user_id", "question_id"}
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OxQuestionResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private OxQuestion oxQuestion;

    @Column(name = "user_answer", nullable = false)
    private boolean userAnswer;

    @Column(name = "is_correct", nullable = false)
    private boolean isCorrect;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public OxQuestionResult(User user, OxQuestion oxQuestion,
                            boolean userAnswer, boolean isCorrect) {
        this.user = user;
        this.oxQuestion = oxQuestion;
        this.userAnswer = userAnswer;
        this.isCorrect = isCorrect;
    }

    public void updateAnswer(boolean userAnswer, boolean isCorrect) {
        this.userAnswer = userAnswer;
        this.isCorrect = isCorrect;
    }

    public void touch() {
        this.updatedAt = LocalDateTime.now();
    }
}
