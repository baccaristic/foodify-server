-- Add multi-language support for restaurants
ALTER TABLE restaurant 
ADD COLUMN name_en VARCHAR(255),
ADD COLUMN name_fr VARCHAR(255),
ADD COLUMN name_ar VARCHAR(255),
ADD COLUMN description_en TEXT,
ADD COLUMN description_fr TEXT,
ADD COLUMN description_ar TEXT;

-- Add multi-language support for menu items
ALTER TABLE menu_item
ADD COLUMN name_en VARCHAR(255),
ADD COLUMN name_fr VARCHAR(255),
ADD COLUMN name_ar VARCHAR(255),
ADD COLUMN description_en TEXT,
ADD COLUMN description_fr TEXT,
ADD COLUMN description_ar TEXT;

-- Add multi-language support for menu categories
ALTER TABLE menu_category
ADD COLUMN name_en VARCHAR(255),
ADD COLUMN name_fr VARCHAR(255),
ADD COLUMN name_ar VARCHAR(255);

-- Add multi-language support for menu option groups
ALTER TABLE menu_option_group
ADD COLUMN name_en VARCHAR(255),
ADD COLUMN name_fr VARCHAR(255),
ADD COLUMN name_ar VARCHAR(255);

-- Add multi-language support for menu item extras
ALTER TABLE menu_item_extra
ADD COLUMN name_en VARCHAR(255),
ADD COLUMN name_fr VARCHAR(255),
ADD COLUMN name_ar VARCHAR(255);
