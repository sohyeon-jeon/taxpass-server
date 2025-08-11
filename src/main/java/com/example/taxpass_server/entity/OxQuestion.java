package com.example.taxpass_server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "ox_questions")
@Getter
@Setter
public class OxQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subject_id", nullable = false)
    private Long subjectId;


    @Column(name = "number", length = 10, nullable = false)
    private String number;

    @Lob
    @Column(name = "question_text", nullable = false)
    private String questionText;

    @Column(name = "answer", nullable = false)
    private boolean answer; // TRUE = O, FALSE = X

    @Lob
    @Column(name = "explanation")
    private String explanation;

//    @JdbcTypeCode(SqlTypes.ARRAY)
//    @Column(name = "tag", columnDefinition = "text[]")
//    private String[] tag;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by_ip", columnDefinition = "inet")
    private String createdByIp;

    @Column(name = "created_by_user_id")
    private String createdByUserId;

    @Column(name = "updated_by_ip", columnDefinition = "inet")
    private String updatedByIp;

    @Column(name = "updated_by_user_id")
    private String updatedByUserId;
}
