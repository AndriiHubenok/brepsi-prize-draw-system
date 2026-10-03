ALTER TABLE activated_promo_codes
    ADD COLUMN desired_article VARCHAR(50) NOT NULL DEFAULT 'OTHER';

ALTER TABLE activated_promo_codes
    ADD COLUMN supermarket VARCHAR(50) NOT NULL DEFAULT 'OTHER';