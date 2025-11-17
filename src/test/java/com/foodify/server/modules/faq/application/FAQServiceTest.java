package com.foodify.server.modules.faq.application;

import com.foodify.server.modules.faq.domain.FAQItem;
import com.foodify.server.modules.faq.domain.FAQSection;
import com.foodify.server.modules.faq.dto.FAQItemRequest;
import com.foodify.server.modules.faq.dto.FAQSectionRequest;
import com.foodify.server.modules.faq.dto.FAQSectionResponse;
import com.foodify.server.modules.faq.repository.FAQItemRepository;
import com.foodify.server.modules.faq.repository.FAQSectionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FAQServiceTest {

    @Mock
    private FAQSectionRepository sectionRepository;

    @Mock
    private FAQItemRepository itemRepository;

    @InjectMocks
    private FAQService faqService;

    @Test
    void testCreateSection() {
        FAQSectionRequest request = new FAQSectionRequest();
        request.setPosition(1);
        request.setNameEn("General");
        request.setNameFr("Général");
        request.setNameAr("عام");

        FAQSection mockSection = new FAQSection();
        mockSection.setId(1L);
        mockSection.setPosition(request.getPosition());
        mockSection.setNameEn(request.getNameEn());
        mockSection.setNameFr(request.getNameFr());
        mockSection.setNameAr(request.getNameAr());

        when(sectionRepository.save(any(FAQSection.class))).thenReturn(mockSection);

        FAQSection result = faqService.createSection(request);

        assertNotNull(result);
        assertEquals(1, result.getPosition());
        assertEquals("General", result.getNameEn());
        assertEquals("Général", result.getNameFr());
        assertEquals("عام", result.getNameAr());
        verify(sectionRepository, times(1)).save(any(FAQSection.class));
    }

    @Test
    void testUpdateSection() {
        Long sectionId = 1L;
        FAQSectionRequest request = new FAQSectionRequest();
        request.setPosition(2);
        request.setNameEn("Updated Section");
        request.setNameFr("Section Mise à jour");
        request.setNameAr("قسم محدث");

        FAQSection existingSection = new FAQSection();
        existingSection.setId(sectionId);

        when(sectionRepository.findById(sectionId)).thenReturn(Optional.of(existingSection));
        when(sectionRepository.save(any(FAQSection.class))).thenReturn(existingSection);

        FAQSection result = faqService.updateSection(sectionId, request);

        assertNotNull(result);
        assertEquals(2, result.getPosition());
        assertEquals("Updated Section", result.getNameEn());
        verify(sectionRepository, times(1)).findById(sectionId);
        verify(sectionRepository, times(1)).save(existingSection);
    }

    @Test
    void testDeleteSection() {
        Long sectionId = 1L;

        when(sectionRepository.existsById(sectionId)).thenReturn(true);
        doNothing().when(sectionRepository).deleteById(sectionId);

        assertDoesNotThrow(() -> faqService.deleteSection(sectionId));
        verify(sectionRepository, times(1)).existsById(sectionId);
        verify(sectionRepository, times(1)).deleteById(sectionId);
    }

    @Test
    void testDeleteSectionNotFound() {
        Long sectionId = 1L;

        when(sectionRepository.existsById(sectionId)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> faqService.deleteSection(sectionId));
        verify(sectionRepository, times(1)).existsById(sectionId);
        verify(sectionRepository, never()).deleteById(sectionId);
    }

    @Test
    void testGetAllSectionsLocalized_English() {
        FAQSection section = new FAQSection();
        section.setId(1L);
        section.setPosition(1);
        section.setNameEn("General");
        section.setNameFr("Général");
        section.setNameAr("عام");
        section.setItems(new ArrayList<>());

        FAQItem item = new FAQItem();
        item.setId(1L);
        item.setSection(section);
        item.setPosition(1);
        item.setQuestionEn("What is this?");
        item.setQuestionFr("Qu'est-ce que c'est?");
        item.setQuestionAr("ما هذا؟");
        item.setAnswerEn("This is an answer");
        item.setAnswerFr("Ceci est une réponse");
        item.setAnswerAr("هذا جواب");

        section.getItems().add(item);

        when(sectionRepository.findAllByOrderByPositionAsc()).thenReturn(List.of(section));

        List<FAQSectionResponse> result = faqService.getAllSectionsLocalized("en");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("General", result.get(0).getName());
        assertEquals(1, result.get(0).getItems().size());
        assertEquals("What is this?", result.get(0).getItems().get(0).getQuestion());
        assertEquals("This is an answer", result.get(0).getItems().get(0).getAnswer());
    }

    @Test
    void testGetAllSectionsLocalized_Arabic() {
        FAQSection section = new FAQSection();
        section.setId(1L);
        section.setPosition(1);
        section.setNameEn("General");
        section.setNameFr("Général");
        section.setNameAr("عام");
        section.setItems(new ArrayList<>());

        FAQItem item = new FAQItem();
        item.setId(1L);
        item.setSection(section);
        item.setPosition(1);
        item.setQuestionEn("What is this?");
        item.setQuestionFr("Qu'est-ce que c'est?");
        item.setQuestionAr("ما هذا؟");
        item.setAnswerEn("This is an answer");
        item.setAnswerFr("Ceci est une réponse");
        item.setAnswerAr("هذا جواب");

        section.getItems().add(item);

        when(sectionRepository.findAllByOrderByPositionAsc()).thenReturn(List.of(section));

        List<FAQSectionResponse> result = faqService.getAllSectionsLocalized("ar");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("عام", result.get(0).getName());
        assertEquals("ما هذا؟", result.get(0).getItems().get(0).getQuestion());
        assertEquals("هذا جواب", result.get(0).getItems().get(0).getAnswer());
    }

    @Test
    void testGetAllSectionsLocalized_French() {
        FAQSection section = new FAQSection();
        section.setId(1L);
        section.setPosition(1);
        section.setNameEn("General");
        section.setNameFr("Général");
        section.setNameAr("عام");
        section.setItems(new ArrayList<>());

        FAQItem item = new FAQItem();
        item.setId(1L);
        item.setSection(section);
        item.setPosition(1);
        item.setQuestionEn("What is this?");
        item.setQuestionFr("Qu'est-ce que c'est?");
        item.setQuestionAr("ما هذا؟");
        item.setAnswerEn("This is an answer");
        item.setAnswerFr("Ceci est une réponse");
        item.setAnswerAr("هذا جواب");

        section.getItems().add(item);

        when(sectionRepository.findAllByOrderByPositionAsc()).thenReturn(List.of(section));

        List<FAQSectionResponse> result = faqService.getAllSectionsLocalized("fr");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Général", result.get(0).getName());
        assertEquals("Qu'est-ce que c'est?", result.get(0).getItems().get(0).getQuestion());
        assertEquals("Ceci est une réponse", result.get(0).getItems().get(0).getAnswer());
    }

    @Test
    void testCreateItem() {
        Long sectionId = 1L;
        FAQItemRequest request = new FAQItemRequest();
        request.setSectionId(sectionId);
        request.setPosition(1);
        request.setQuestionEn("Question?");
        request.setQuestionFr("Question?");
        request.setQuestionAr("سؤال؟");
        request.setAnswerEn("Answer.");
        request.setAnswerFr("Réponse.");
        request.setAnswerAr("جواب.");

        FAQSection section = new FAQSection();
        section.setId(sectionId);

        FAQItem mockItem = new FAQItem();
        mockItem.setId(1L);
        mockItem.setSection(section);

        when(sectionRepository.findById(sectionId)).thenReturn(Optional.of(section));
        when(itemRepository.save(any(FAQItem.class))).thenReturn(mockItem);

        FAQItem result = faqService.createItem(request);

        assertNotNull(result);
        assertNotNull(result.getSection());
        verify(sectionRepository, times(1)).findById(sectionId);
        verify(itemRepository, times(1)).save(any(FAQItem.class));
    }
}
