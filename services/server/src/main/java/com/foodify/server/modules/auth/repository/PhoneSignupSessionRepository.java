package com.foodify.server.modules.auth.repository;

import com.foodify.server.modules.auth.domain.PhoneSignupSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PhoneSignupSessionRepository extends JpaRepository<PhoneSignupSession, Long> {
    Optional<PhoneSignupSession> findBySessionId(String sessionId);

    List<PhoneSignupSession> findAllByPhoneNumberAndCompletedFalse(String phoneNumber);
}
