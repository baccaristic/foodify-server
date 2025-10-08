package com.foodify.addressesservice.addresses.domain.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Converter(autoApply = false)
public class JsonNodeAttributeConverter implements AttributeConverter<JsonNode, String> {

    private static final Logger log = LoggerFactory.getLogger(JsonNodeAttributeConverter.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(JsonNode attribute) {
        if (attribute == null || attribute.isNull() || attribute.isMissingNode()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize JsonNode attribute", e);
            throw new IllegalArgumentException("Unable to serialize JSON attribute", e);
        }
    }

    @Override
    public JsonNode convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readTree(dbData);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize JsonNode attribute", e);
            throw new IllegalArgumentException("Unable to deserialize JSON attribute", e);
        }
    }
}
