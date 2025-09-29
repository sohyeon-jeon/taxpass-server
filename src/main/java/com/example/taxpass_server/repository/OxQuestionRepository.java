package com.example.taxpass_server.repository;

import com.example.taxpass_server.entity.OxQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OxQuestionRepository extends JpaRepository<OxQuestion, Long> {

    @Query(value = """
    SELECT subject_id,
        number,
        question_text,
        answer,
        explanation,
        tag
    FROM ox_questions
    WHERE subject_id = :subjectId
    order by subject_id,number::int
""", nativeQuery = true)
    List<Object[]> findOxQuestionsBySubjectId(@Param("subjectId") Long subjectId);
}
