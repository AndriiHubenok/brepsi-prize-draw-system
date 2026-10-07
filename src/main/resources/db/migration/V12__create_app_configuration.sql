CREATE TABLE app_configuration
(
    key        VARCHAR(64) PRIMARY KEY,
    value      VARCHAR(255) NOT NULL,
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

INSERT INTO app_configuration (key, value)
VALUES ('DRAW_ALGORITHM', 'WINNING_MOMENTS');