package com.foodify.server.modules.faq.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FAQSectionRequest {
    
    @NotNull(message = "Position is required")
    private Integer position;
    
    @NotBlank(message = "English name is required")
    private String nameEn;
    
    @NotBlank(message = "French name is required")
    private String nameFr;
    
    @NotBlank(message = "Arabic name is required")
    private String nameAr;
}
