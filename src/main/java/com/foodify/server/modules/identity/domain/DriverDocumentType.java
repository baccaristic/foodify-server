package com.foodify.server.modules.identity.domain;

import lombok.Getter;

import java.util.Locale;

@Getter
public enum DriverDocumentType {
    ID_CARD("ID Card", "Upload a clear photo of your government issued ID (front and back)."),
    PROFILE_PICTURE("Picture", "Upload a clear photo of yourself with your face fully visible."),
    BULLETIN_N3("Bulletin N3", "Upload your latest police record (Bulletin N3)."),
    UTILITY_BILL("Electricity or water bill", "Provide a recent electricity or water bill that confirms your address."),
    PATENT_NUMBER("Patent number", "Upload a proof of your transport patent or professional license.");

    private final String title;
    private final String instructions;

    DriverDocumentType(String title, String instructions) {
        this.title = title;
        this.instructions = instructions;
    }

    public static DriverDocumentType fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Document type is required");
        }
        String normalized = value.trim().toUpperCase(Locale.ROOT)
                .replace("-", "_")
                .replace(" ", "_");
        return switch (normalized) {
            case "PICTURE" -> PROFILE_PICTURE;
            case "BULLETIN", "BULLETIN_3", "BULLETIN_N3", "BULLETIN_NO3" -> BULLETIN_N3;
            case "UTILITY", "UTILITY_BILL", "ELECTRICITY", "ELECTRICITY_BILL", "WATER_BILL" -> UTILITY_BILL;
            case "PATENT", "PATENT_NUMBER", "LICENSE" -> PATENT_NUMBER;
            case "ID", "ID_CARD", "IDENTITY_CARD", "IDCARD" -> ID_CARD;
            default -> DriverDocumentType.valueOf(normalized);
        };
    }
}
