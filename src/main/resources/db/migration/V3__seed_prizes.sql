DO
$$
DECLARE
v_start TIMESTAMPTZ := '2026-06-01 00:00:00+02';
    v_end
TIMESTAMPTZ := '2026-10-31 23:59:59+02';
    v_diff
INTERVAL;
BEGIN
    v_diff
:= v_end - v_start;

-- 30 000 T-Shirts
INSERT INTO prize_pool (category, status, release_time)
SELECT 'TSHIRT', 'AVAILABLE', v_start + (random() * v_diff)
FROM generate_series(1, 30000);

-- 20 000 Shopper bags
INSERT INTO prize_pool (category, status, release_time)
SELECT 'SHOPPER', 'AVAILABLE', v_start + (random() * v_diff)
FROM generate_series(1, 20000);

-- 20 000 Socks
INSERT INTO prize_pool (category, status, release_time)
SELECT 'SOCKS', 'AVAILABLE', v_start + (random() * v_diff)
FROM generate_series(1, 20000);

-- 5 000 Caps
INSERT INTO prize_pool (category, status, release_time)
SELECT 'CAP', 'AVAILABLE', v_start + (random() * v_diff)
FROM generate_series(1, 5000);
END $$;