# FAQ API Documentation

## Overview

The FAQ (Frequently Asked Questions) feature provides a multi-language system for managing and displaying frequently asked questions organized by sections. The system supports three languages: English (en), Arabic (ar), and French (fr).

## Architecture

The FAQ system consists of two main entities:

1. **FAQ Section**: Represents a category of questions (e.g., "General", "Ordering", "Payments")
2. **FAQ Item**: Represents individual question-answer pairs within a section

## Database Schema

### Tables

#### `faq_section`
- `id`: Primary key
- `position`: Integer for ordering sections (unique)
- `name_en`, `name_fr`, `name_ar`: Section names in three languages
- `created_at`, `updated_at`: Timestamps

#### `faq_item`
- `id`: Primary key
- `section_id`: Foreign key to faq_section
- `position`: Integer for ordering items within a section
- `question_en`, `question_fr`, `question_ar`: Questions in three languages
- `answer_en`, `answer_fr`, `answer_ar`: Answers in three languages
- `created_at`, `updated_at`: Timestamps

## API Endpoints

### Admin Endpoints (Requires ROLE_ADMIN)

All admin endpoints are prefixed with `/api/admin/faq`

#### Section Management

**Create Section**
```
POST /api/admin/faq/sections
Content-Type: application/json

{
  "position": 1,
  "nameEn": "General Questions",
  "nameFr": "Questions Générales",
  "nameAr": "أسئلة عامة"
}
```

**Update Section**
```
PUT /api/admin/faq/sections/{id}
Content-Type: application/json

{
  "position": 1,
  "nameEn": "General Questions (Updated)",
  "nameFr": "Questions Générales (Mise à jour)",
  "nameAr": "أسئلة عامة (محدث)"
}
```

**Get Section**
```
GET /api/admin/faq/sections/{id}
```

**Get All Sections**
```
GET /api/admin/faq/sections
```

**Delete Section**
```
DELETE /api/admin/faq/sections/{id}
```

#### Item Management

**Create Item**
```
POST /api/admin/faq/items
Content-Type: application/json

{
  "sectionId": 1,
  "position": 1,
  "questionEn": "How do I place an order?",
  "questionFr": "Comment passer une commande?",
  "questionAr": "كيف أضع طلب؟",
  "answerEn": "To place an order, browse the menu and add items to your cart.",
  "answerFr": "Pour passer une commande, parcourez le menu et ajoutez des articles à votre panier.",
  "answerAr": "لتقديم طلب، تصفح القائمة وأضف عناصر إلى سلة التسوق الخاصة بك."
}
```

**Update Item**
```
PUT /api/admin/faq/items/{id}
Content-Type: application/json

{
  "sectionId": 1,
  "position": 1,
  "questionEn": "How do I place an order? (Updated)",
  "questionFr": "Comment passer une commande? (Mise à jour)",
  "questionAr": "كيف أضع طلب؟ (محدث)",
  "answerEn": "Updated answer...",
  "answerFr": "Réponse mise à jour...",
  "answerAr": "إجابة محدثة..."
}
```

**Get Item**
```
GET /api/admin/faq/items/{id}
```

**Get Items by Section**
```
GET /api/admin/faq/sections/{sectionId}/items
```

**Delete Item**
```
DELETE /api/admin/faq/items/{id}
```

### Client Endpoints (Public Access)

Client endpoints are prefixed with `/api/client/faq`

**Get All FAQs (Localized)**
```
GET /api/client/faq?lang=en
GET /api/client/faq?lang=ar
GET /api/client/faq?lang=fr
```

Default language is English if not specified.

**Response Format:**
```json
[
  {
    "id": 1,
    "position": 1,
    "name": "General Questions",
    "items": [
      {
        "id": 1,
        "sectionId": 1,
        "position": 1,
        "question": "How do I place an order?",
        "answer": "To place an order, browse the menu and add items to your cart."
      }
    ]
  }
]
```

## Features

### Multi-Language Support
- All content (section names, questions, answers) is stored in three languages
- Clients can request content in their preferred language using the `lang` parameter
- Fallback to English if an invalid language code is provided

### Ordering
- Sections and items are ordered by their `position` field
- Sections must have unique positions
- Items are ordered within each section

### Cascade Delete
- Deleting a section automatically deletes all its items
- Enforced at database level with `ON DELETE CASCADE`

### Security
- Admin endpoints require `ROLE_ADMIN` authority
- Client endpoints are publicly accessible

## Usage Examples

### Setting up FAQs

1. Create sections in the desired order:
```bash
# Create General section
curl -X POST http://localhost:8080/api/admin/faq/sections \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <admin-token>" \
  -d '{
    "position": 1,
    "nameEn": "General",
    "nameFr": "Général",
    "nameAr": "عام"
  }'

# Create Ordering section
curl -X POST http://localhost:8080/api/admin/faq/sections \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <admin-token>" \
  -d '{
    "position": 2,
    "nameEn": "Ordering",
    "nameFr": "Commande",
    "nameAr": "الطلب"
  }'
```

2. Add items to sections:
```bash
curl -X POST http://localhost:8080/api/admin/faq/items \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <admin-token>" \
  -d '{
    "sectionId": 1,
    "position": 1,
    "questionEn": "What is Foodify?",
    "questionFr": "Qu'\''est-ce que Foodify?",
    "questionAr": "ما هو Foodify؟",
    "answerEn": "Foodify is a food delivery platform...",
    "answerFr": "Foodify est une plateforme de livraison de nourriture...",
    "answerAr": "Foodify هي منصة توصيل الطعام..."
  }'
```

3. Retrieve FAQs in client app:
```bash
# Get English FAQs
curl http://localhost:8080/api/client/faq?lang=en

# Get Arabic FAQs
curl http://localhost:8080/api/client/faq?lang=ar

# Get French FAQs
curl http://localhost:8080/api/client/faq?lang=fr
```

## Testing

Unit tests are provided in `FAQServiceTest.java` covering:
- CRUD operations for sections and items
- Localization for all three languages
- Error handling for not found cases
- Cascade deletion behavior

Run tests with:
```bash
./gradlew test --tests "com.foodify.server.modules.faq.application.FAQServiceTest"
```

## Database Migration

The FAQ tables are created by Flyway migration script `V20__create_faq_tables.sql`.

To apply the migration:
```bash
./gradlew flywayMigrate
```
