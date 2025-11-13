package com.foodify.server.modules.faq.repository;

import com.foodify.server.modules.faq.domain.FAQItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FAQItemRepository extends JpaRepository<FAQItem, Long> {
    
    List<FAQItem> findBySectionIdOrderByPositionAsc(Long sectionId);
}
