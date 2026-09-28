CREATE TABLE prize_pool
(
    id             BIGSERIAL PRIMARY KEY,
    category       VARCHAR(50) NOT NULL,
    status         VARCHAR(50) NOT NULL DEFAULT 'AVAILABLE',
    release_time   TIMESTAMPTZ NOT NULL,
    winner_user_id VARCHAR(255),
    promo_code     VARCHAR(255),
    claimed_time   TIMESTAMPTZ
);