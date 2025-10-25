package com.foodify.authservice.modules.auth.repository;

import com.foodify.authservice.modules.auth.domain.EmailSignupSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmailSignupSessionRepository extends JpaRepository<EmailSignupSession, Long> {
    Optional<EmailSignupSession> findBySessionId(String sessionId);

    List<EmailSignupSession> findAllByEmailAndCompletedFalse(String email);
}
