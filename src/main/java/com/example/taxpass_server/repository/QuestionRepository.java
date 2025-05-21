package com.example.taxpass_server.repository;

import com.example.taxpass_server.entity.Question;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
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
                                                      ORDER BY\s
                                                        CASE\s
                                                          WHEN c.choice_type = 'table'\s
                                                          THEN regexp_replace(c.content::jsonb ->> '번호', '[^\\d]', '', 'g')::int
                                                          ELSE c.choice_index
                                                        END
                                                    ) AS choices,
                                                    a.correct_answer,
                                                    a.explanation
                                                  FROM questions q
                                                  JOIN choices c ON q.id = c.question_id
                                                  JOIN answers a ON q.id = a.question_id
                                                  GROUP BY
                                                    q.id,
                                                    q.problem_group_id,
                                                    q."number",
                                                    q.exam_year,
                                                    q.exam_name,
                                                    q.description,
                                                    a.correct_answer,
                                                    a.explanation
                                                  ORDER BY q.id;
        """, nativeQuery = true)
    List<Object[]> findQuestionBundle();
}
