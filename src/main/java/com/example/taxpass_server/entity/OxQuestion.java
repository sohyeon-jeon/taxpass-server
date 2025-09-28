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

    

    @EmbeddedId
    private OxQuestionId id;

    @Column(name = "question_text", nullable = false, columnDefinition = "text")
    private String questionText;

    @Column(name = "answer", nullable = false)
    private boolean answer; // TRUE = O, FALSE = X

    @Column(name = "explanation", columnDefinition = "text")
    private String explanation;
//
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "tag", columnDefinition = "text[]")
    private String[] tag;

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


//    (subject_id,number) 복합키 설정
    public void setSubjectId(long subjectId) {
        if (this.id == null) {
            this.id = new OxQuestionId();
        }
        this.id.setSubjectId(subjectId);
    }

    public void setNumber(String number) {
        if (this.id == null) {
            this.id = new OxQuestionId();
        }
        this.id.setNumber(number);
    }

    public long getSubjectId() {
        if (id == null || id.getSubjectId() == null) {
            return 0L;
        }
        return id.getSubjectId();
    }

    public String getNumber() {
        return id != null ? id.getNumber() : null;
    }
}
