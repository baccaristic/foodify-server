package com.foodify.server.modules.faq.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "faq_item",
        indexes = {
                @Index(name = "idx_faq_item_section", columnList = "section_id"),
                @Index(name = "idx_faq_item_position", columnList = "position")
        }
)
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FAQItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "section_id", nullable = false)
    private FAQSection section;
    
    @Column(name = "position", nullable = false)
    private Integer position;
    
    @Column(name = "question_en", nullable = false, columnDefinition = "TEXT")
    private String questionEn;
    
    @Column(name = "question_fr", nullable = false, columnDefinition = "TEXT")
    private String questionFr;
    
    @Column(name = "question_ar", nullable = false, columnDefinition = "TEXT")
    private String questionAr;
    
    @Column(name = "answer_en", nullable = false, columnDefinition = "TEXT")
    private String answerEn;
    
    @Column(name = "answer_fr", nullable = false, columnDefinition = "TEXT")
    private String answerFr;
    
    @Column(name = "answer_ar", nullable = false, columnDefinition = "TEXT")
    private String answerAr;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (this.createdAt == null) {
            this.createdAt = now;
        }
        this.updatedAt = now;
    }
    
    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
