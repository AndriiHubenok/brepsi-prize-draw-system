CREATE TABLE activated_promo_codes
(
    code         VARCHAR(10) PRIMARY KEY,
    serial_id    INTEGER      NOT NULL UNIQUE,
    user_id      VARCHAR(255) NOT NULL,
    activated_at TIMESTAMPTZ  NOT NULL
);

CREATE UNIQUE INDEX idx_activated_serial ON activated_promo_codes (serial_id);