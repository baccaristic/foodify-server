package com.foodify.server.modules.faq.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FAQItemRequest {
    
    @NotNull(message = "Section ID is required")
    private Long sectionId;
    
    @NotNull(message = "Position is required")
    private Integer position;
    
    @NotBlank(message = "English question is required")
    private String questionEn;
    
    @NotBlank(message = "French question is required")
    private String questionFr;
    
    @NotBlank(message = "Arabic question is required")
    private String questionAr;
    
    @NotBlank(message = "English answer is required")
    private String answerEn;
    
    @NotBlank(message = "French answer is required")
    private String answerFr;
    
    @NotBlank(message = "Arabic answer is required")
    private String answerAr;
}
