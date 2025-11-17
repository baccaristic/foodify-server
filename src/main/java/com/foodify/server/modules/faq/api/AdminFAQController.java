package com.foodify.server.modules.faq.api;

import com.foodify.server.modules.faq.application.FAQService;
import com.foodify.server.modules.faq.domain.FAQItem;
import com.foodify.server.modules.faq.domain.FAQSection;
import com.foodify.server.modules.faq.dto.FAQItemRequest;
import com.foodify.server.modules.faq.dto.FAQSectionRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/faq")
@Tag(name = "Admin FAQ", description = "FAQ management APIs for administrators")
public class AdminFAQController {
    
    private final FAQService faqService;
    
    // Section endpoints
    
    @PostMapping("/sections")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new FAQ section")
    public FAQSection createSection(@Valid @RequestBody FAQSectionRequest request) {
        return faqService.createSection(request);
    }
    
    @PutMapping("/sections/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Update an existing FAQ section")
    public FAQSection updateSection(
            @PathVariable Long id,
            @Valid @RequestBody FAQSectionRequest request) {
        return faqService.updateSection(id, request);
    }
    
    @DeleteMapping("/sections/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete an FAQ section")
    public void deleteSection(@PathVariable Long id) {
        faqService.deleteSection(id);
    }
    
    @GetMapping("/sections/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Get an FAQ section by ID")
    public FAQSection getSection(@PathVariable Long id) {
        return faqService.getSection(id);
    }
    
    @GetMapping("/sections")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Get all FAQ sections")
    public List<FAQSection> getAllSections() {
        return faqService.getAllSections();
    }
    
    // Item endpoints
    
    @PostMapping("/items")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new FAQ item")
    public FAQItem createItem(@Valid @RequestBody FAQItemRequest request) {
        return faqService.createItem(request);
    }
    
    @PutMapping("/items/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Update an existing FAQ item")
    public FAQItem updateItem(
            @PathVariable Long id,
            @Valid @RequestBody FAQItemRequest request) {
        return faqService.updateItem(id, request);
    }
    
    @DeleteMapping("/items/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete an FAQ item")
    public void deleteItem(@PathVariable Long id) {
        faqService.deleteItem(id);
    }
    
    @GetMapping("/items/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Get an FAQ item by ID")
    public FAQItem getItem(@PathVariable Long id) {
        return faqService.getItem(id);
    }
    
    @GetMapping("/sections/{sectionId}/items")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Get all FAQ items for a section")
    public List<FAQItem> getItemsBySection(@PathVariable Long sectionId) {
        return faqService.getItemsBySection(sectionId);
    }
}
