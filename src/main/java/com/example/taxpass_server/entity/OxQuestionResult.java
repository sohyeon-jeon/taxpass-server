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
@Table(name = "ox_question_results")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OxQuestionResult {

    @EmbeddedId
    private OxQuestionResultId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "kakaoId", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "subject_id", referencedColumnName = "subject_id", insertable = false, updatable = false),
            @JoinColumn(name = "number", referencedColumnName = "number", insertable = false, updatable = false)
    })
    private OxQuestion oxQuestion;

    @Column(name = "user_answer", nullable = false)
    private boolean userAnswer;

    @Column(name = "is_correct", nullable = false)
    private boolean isCorrect;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public OxQuestionResult(OxQuestionResultId id, boolean userAnswer, boolean isCorrect) {
        this.id = id;
        this.userAnswer = userAnswer;
        this.isCorrect = isCorrect;
    }
}
