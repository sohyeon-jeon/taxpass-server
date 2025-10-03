package com.example.taxpass_server.repository;

import com.example.taxpass_server.entity.Question;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(value = """
    SELECT
      q.id,
      q.problem_group_id,
      q."number",
      q.exam_year,
      q.exam_name,
      q.description,
      json_agg(
        json_build_object(
          'choice_index', c.choice_index,
          'type', c.choice_type,
          'content',
          CASE
            WHEN c.choice_type = 'table' THEN c.content::jsonb
            ELSE to_jsonb(c.content)
          END
        )
        ORDER BY
          CASE
            WHEN c.choice_type = 'table' THEN
              CASE c.content::jsonb ->> 'number'
                WHEN '①' THEN 1
                WHEN '②' THEN 2
                WHEN '③' THEN 3
                WHEN '④' THEN 4
                WHEN '⑤' THEN 5
                ELSE 999
              END
            ELSE c.choice_index
          END
      ) AS choices,
      a.correct_answer,
      a.explanation
    FROM questions q
    JOIN choices c ON q.id = c.question_id
    JOIN answers a ON q.id = a.question_id
    JOIN problem_groups p ON  q.problem_group_id= p.id
    WHERE p.subjects_id = :subjectId
    GROUP BY
      q.id,
      q.problem_group_id,
      q."number",
      q.exam_year,
      q.exam_name,
      q.description,
      a.correct_answer,
      a.explanation
    ORDER BY q.id
    """, nativeQuery = true)

    List<Object[]> findQuestionBundleBySubjectId(@Param("subjectId") Long subjectId);
}
