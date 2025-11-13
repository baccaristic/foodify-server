package com.foodify.server.modules.faq.dto;

import lombok.Data;

@Data
public class FAQItemResponse {
    
    private Long id;
    private Long sectionId;
    private Integer position;
    private String question;
    private String answer;
}
