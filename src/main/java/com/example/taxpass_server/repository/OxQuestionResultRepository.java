package com.example.taxpass_server.repository;

import com.example.taxpass_server.entity.OxQuestion;
import com.example.taxpass_server.entity.OxQuestionResult;
import com.example.taxpass_server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OxQuestionResultRepository
        extends JpaRepository<OxQuestionResult, Long> {

    Optional<OxQuestionResult> findByUserAndOxQuestion(User user, OxQuestion oxQuestion);
}

