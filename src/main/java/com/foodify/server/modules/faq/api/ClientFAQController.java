package com.foodify.server.modules.faq.api;

import com.foodify.server.modules.faq.application.FAQService;
import com.foodify.server.modules.faq.dto.FAQSectionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/client/faq")
@Tag(name = "Client FAQ", description = "FAQ retrieval APIs for clients")
public class ClientFAQController {
    
    private final FAQService faqService;
    
    @GetMapping
    @Operation(summary = "Get all FAQ sections with localized content", 
               description = "Returns all FAQ sections and items translated to the requested language (en, ar, or fr)")
    public List<FAQSectionResponse> getAllFAQs(
            @Parameter(description = "Language code (en, ar, or fr)", example = "en")
            @RequestParam(defaultValue = "en") String lang) {
        return faqService.getAllSectionsLocalized(lang);
    }
}
