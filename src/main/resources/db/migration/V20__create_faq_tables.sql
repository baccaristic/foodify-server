-- Create FAQ Section table
CREATE TABLE faq_section (
    id BIGSERIAL PRIMARY KEY,
    position INTEGER NOT NULL,
    name_en VARCHAR(255) NOT NULL,
    name_fr VARCHAR(255) NOT NULL,
    name_ar VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT uk_faq_section_position UNIQUE (position)
);

-- Create FAQ Item table
CREATE TABLE faq_item (
    id BIGSERIAL PRIMARY KEY,
    section_id BIGINT NOT NULL,
    position INTEGER NOT NULL,
    question_en TEXT NOT NULL,
    question_fr TEXT NOT NULL,
    question_ar TEXT NOT NULL,
    answer_en TEXT NOT NULL,
    answer_fr TEXT NOT NULL,
    answer_ar TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_faq_item_section FOREIGN KEY (section_id) REFERENCES faq_section(id) ON DELETE CASCADE
);

-- Create indexes
CREATE INDEX idx_faq_section_position ON faq_section(position);
CREATE INDEX idx_faq_item_section ON faq_item(section_id);
CREATE INDEX idx_faq_item_position ON faq_item(position);
