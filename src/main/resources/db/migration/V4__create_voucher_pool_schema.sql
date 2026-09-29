CREATE TABLE voucher_pool
(
    id             BIGSERIAL PRIMARY KEY,
    code           VARCHAR(6)  NOT NULL UNIQUE,
    status         VARCHAR(50) NOT NULL DEFAULT 'AVAILABLE',
    winner_user_id VARCHAR(255),
    promo_code     VARCHAR(255),
    claimed_time   TIMESTAMPTZ
);

CREATE INDEX idx_voucher_pool_available
    ON voucher_pool (id) WHERE status = 'AVAILABLE';