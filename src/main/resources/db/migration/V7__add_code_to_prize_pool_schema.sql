ALTER TABLE prize_pool
    ADD COLUMN code VARCHAR(20);

ALTER TABLE prize_pool
    ADD CONSTRAINT uk_prize_pool_code UNIQUE (code);