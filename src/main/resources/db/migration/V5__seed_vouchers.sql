CREATE OR REPLACE FUNCTION generate_voucher_code(p_length INT DEFAULT 6)
    RETURNS TEXT AS
$$
DECLARE
    v_chars CONSTANT TEXT := '23456789ABCDEFGHJKLMNPQRSTUVWXYZ';
    v_result         TEXT := '';
BEGIN
    FOR i IN 1..p_length
        LOOP
            v_result := v_result || substr(v_chars, floor(random() * 32 + 1)::INT, 1);
        END LOOP;
    RETURN v_result;
END;
$$ LANGUAGE plpgsql;

DO
$$
    DECLARE
        v_target_count  INT := 1000000;
        v_current_count INT;
    BEGIN
        SELECT count(*) INTO v_current_count FROM voucher_pool;

        WHILE v_current_count < v_target_count
            LOOP
                INSERT INTO voucher_pool (code, status)
                SELECT generate_voucher_code(6),
                       'AVAILABLE'
                FROM generate_series(1, (v_target_count - v_current_count))
                ON CONFLICT (code) DO NOTHING;

                SELECT count(*) INTO v_current_count FROM voucher_pool;
            END LOOP;
    END
$$;

DROP FUNCTION IF EXISTS generate_voucher_code(INT);