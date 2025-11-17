package com.foodify.server.modules.faq.dto;

import lombok.Data;

import java.util.List;

@Data
public class FAQSectionResponse {
    
    private Long id;
    private Integer position;
    private String name;
    private List<FAQItemResponse> items;
}
