DO
$$
    DECLARE
        v_start_date DATE := '2026-06-01';
        v_total_days INT  := 153; -- 01.06.2026 - 31.10.2026
        r            RECORD;
    BEGIN

        FOR r IN
            SELECT 'TSHIRT' AS cat, 30000 AS qty
            UNION ALL
            SELECT 'SHOPPER', 20000
            UNION ALL
            SELECT 'SOCKS', 20000
            UNION ALL
            SELECT 'CAP', 5000
            LOOP
                INSERT INTO prize_pool (category, status, release_time)
                SELECT r.cat,
                       'AVAILABLE',
                       ((v_start_date + (floor(random() * v_total_days))::INT)::TEXT || ' ' ||
                        CASE
                            -- 3% 00:00:00 - 07:59:59
                            WHEN random() < 0.03 THEN
                                ('00:00:00'::TIME + (random() * INTERVAL '7 hours 59 minutes 59 seconds'))::TEXT
                            -- 97% 08:00:00 - 23:59:59
                            ELSE
                                ('08:00:00'::TIME + (random() * INTERVAL '15 hours 59 minutes 59 seconds'))::TEXT
                            END
                           )::TIMESTAMP AT TIME ZONE 'Europe/Berlin'
                FROM generate_series(1, r.qty);
            END LOOP;
    END
$$;