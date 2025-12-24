package com.example.taxpass_server.repository;

import com.example.taxpass_server.entity.OxQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OxQuestionRepository extends JpaRepository<OxQuestion, Long> {

    @Query(value = """
                SELECT
                    q.id,
                    q.subject_id,
                    q.main_theme,
                    q.theme,
                    q.number,
                    q.question_title,
                    q.question_text,
                    q.answer,
                    q.explanation
                FROM ox_questions q
                LEFT JOIN ox_question_results r
                       ON q.id = r.question_id     
                      AND r.user_id = :userId
                WHERE q.subject_id = :subjectId
                  AND (r.user_id IS NULL OR r.is_correct = false)
                ORDER BY q.subject_id, q.number::int
            """, nativeQuery = true)
    List<Object[]> findOxQuestionsBySubjectId(
            @Param("subjectId") Long subjectId,
            @Param("userId") Long userId
    );
}
