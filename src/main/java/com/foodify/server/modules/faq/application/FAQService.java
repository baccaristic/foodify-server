package com.foodify.server.modules.faq.application;

import com.foodify.server.modules.faq.domain.FAQItem;
import com.foodify.server.modules.faq.domain.FAQSection;
import com.foodify.server.modules.faq.dto.FAQItemRequest;
import com.foodify.server.modules.faq.dto.FAQItemResponse;
import com.foodify.server.modules.faq.dto.FAQSectionRequest;
import com.foodify.server.modules.faq.dto.FAQSectionResponse;
import com.foodify.server.modules.faq.repository.FAQItemRepository;
import com.foodify.server.modules.faq.repository.FAQSectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class FAQService {
    
    private final FAQSectionRepository sectionRepository;
    private final FAQItemRepository itemRepository;
    
    // Section CRUD operations
    
    @Transactional
    public FAQSection createSection(FAQSectionRequest request) {
        FAQSection section = new FAQSection();
        section.setPosition(request.getPosition());
        section.setNameEn(request.getNameEn());
        section.setNameFr(request.getNameFr());
        section.setNameAr(request.getNameAr());
        return sectionRepository.save(section);
    }
    
    @Transactional
    public FAQSection updateSection(Long id, FAQSectionRequest request) {
        FAQSection section = sectionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "FAQ Section not found"));
        
        section.setPosition(request.getPosition());
        section.setNameEn(request.getNameEn());
        section.setNameFr(request.getNameFr());
        section.setNameAr(request.getNameAr());
        
        return sectionRepository.save(section);
    }
    
    @Transactional
    public void deleteSection(Long id) {
        if (!sectionRepository.existsById(id)) {
            throw new ResponseStatusException(NOT_FOUND, "FAQ Section not found");
        }
        sectionRepository.deleteById(id);
    }
    
    public FAQSection getSection(Long id) {
        return sectionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "FAQ Section not found"));
    }
    
    public List<FAQSection> getAllSections() {
        return sectionRepository.findAllByOrderByPositionAsc();
    }
    
    // Item CRUD operations
    
    @Transactional
    public FAQItem createItem(FAQItemRequest request) {
        FAQSection section = sectionRepository.findById(request.getSectionId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "FAQ Section not found"));
        
        FAQItem item = new FAQItem();
        item.setSection(section);
        item.setPosition(request.getPosition());
        item.setQuestionEn(request.getQuestionEn());
        item.setQuestionFr(request.getQuestionFr());
        item.setQuestionAr(request.getQuestionAr());
        item.setAnswerEn(request.getAnswerEn());
        item.setAnswerFr(request.getAnswerFr());
        item.setAnswerAr(request.getAnswerAr());
        
        return itemRepository.save(item);
    }
    
    @Transactional
    public FAQItem updateItem(Long id, FAQItemRequest request) {
        FAQItem item = itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "FAQ Item not found"));
        
        if (request.getSectionId() != null && !request.getSectionId().equals(item.getSection().getId())) {
            FAQSection newSection = sectionRepository.findById(request.getSectionId())
                    .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "FAQ Section not found"));
            item.setSection(newSection);
        }
        
        item.setPosition(request.getPosition());
        item.setQuestionEn(request.getQuestionEn());
        item.setQuestionFr(request.getQuestionFr());
        item.setQuestionAr(request.getQuestionAr());
        item.setAnswerEn(request.getAnswerEn());
        item.setAnswerFr(request.getAnswerFr());
        item.setAnswerAr(request.getAnswerAr());
        
        return itemRepository.save(item);
    }
    
    @Transactional
    public void deleteItem(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new ResponseStatusException(NOT_FOUND, "FAQ Item not found");
        }
        itemRepository.deleteById(id);
    }
    
    public FAQItem getItem(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "FAQ Item not found"));
    }
    
    public List<FAQItem> getItemsBySection(Long sectionId) {
        return itemRepository.findBySectionIdOrderByPositionAsc(sectionId);
    }
    
    // Client API - localized responses
    
    public List<FAQSectionResponse> getAllSectionsLocalized(String language) {
        List<FAQSection> sections = sectionRepository.findAllByOrderByPositionAsc();
        return sections.stream()
                .map(section -> toSectionResponse(section, language))
                .collect(Collectors.toList());
    }
    
    private FAQSectionResponse toSectionResponse(FAQSection section, String language) {
        FAQSectionResponse response = new FAQSectionResponse();
        response.setId(section.getId());
        response.setPosition(section.getPosition());
        response.setName(getLocalizedName(section, language));
        
        List<FAQItemResponse> itemResponses = section.getItems().stream()
                .map(item -> toItemResponse(item, language))
                .collect(Collectors.toList());
        response.setItems(itemResponses);
        
        return response;
    }
    
    private FAQItemResponse toItemResponse(FAQItem item, String language) {
        FAQItemResponse response = new FAQItemResponse();
        response.setId(item.getId());
        response.setSectionId(item.getSection().getId());
        response.setPosition(item.getPosition());
        response.setQuestion(getLocalizedQuestion(item, language));
        response.setAnswer(getLocalizedAnswer(item, language));
        return response;
    }
    
    private String getLocalizedName(FAQSection section, String language) {
        if (language == null) {
            language = "en";
        }
        return switch (language.toLowerCase()) {
            case "ar" -> section.getNameAr();
            case "fr" -> section.getNameFr();
            default -> section.getNameEn();
        };
    }
    
    private String getLocalizedQuestion(FAQItem item, String language) {
        if (language == null) {
            language = "en";
        }
        return switch (language.toLowerCase()) {
            case "ar" -> item.getQuestionAr();
            case "fr" -> item.getQuestionFr();
            default -> item.getQuestionEn();
        };
    }
    
    private String getLocalizedAnswer(FAQItem item, String language) {
        if (language == null) {
            language = "en";
        }
        return switch (language.toLowerCase()) {
            case "ar" -> item.getAnswerAr();
            case "fr" -> item.getAnswerFr();
            default -> item.getAnswerEn();
        };
    }
}
