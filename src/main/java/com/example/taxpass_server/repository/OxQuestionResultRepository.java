package com.example.taxpass_server.repository;

import com.example.taxpass_server.entity.OxQuestionResult;
import com.example.taxpass_server.entity.OxQuestionResultId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OxQuestionResultRepository extends JpaRepository<OxQuestionResult, OxQuestionResultId> {
}
