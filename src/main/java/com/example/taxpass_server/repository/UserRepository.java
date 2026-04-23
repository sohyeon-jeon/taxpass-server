package com.example.taxpass_server.repository;

import com.example.taxpass_server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByKakaoId(Long kakaoId);

    @Modifying
    @Query("""
        UPDATE User u
        SET u.cnt = u.cnt + :pages
        WHERE u.id = :userId
          AND u.cnt + :pages <= 5
    """)
    int increaseCntIfPossible(@Param("userId") Long userId,
                              @Param("pages") int pages);
}

