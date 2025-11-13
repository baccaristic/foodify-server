package com.foodify.server.modules.faq.repository;

import com.foodify.server.modules.faq.domain.FAQSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FAQSectionRepository extends JpaRepository<FAQSection, Long> {
    
    List<FAQSection> findAllByOrderByPositionAsc();
}
