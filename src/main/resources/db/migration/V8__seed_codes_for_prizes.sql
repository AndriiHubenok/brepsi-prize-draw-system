DO $$
DECLARE
v_alphabet CONSTANT TEXT := '23456789ABCDEFGHJKLMNPQRSTUVWXYZ';
    v_mult     CONSTANT BIGINT := 712398471;
    v_mask     CONSTANT BIGINT := 1073741823;
BEGIN
UPDATE prize_pool
SET code = 'BREPSI-' || (
    substr(v_alphabet, ((( (id * v_mult) & v_mask)        ) % 32 + 1)::INT, 1) ||
    substr(v_alphabet, ((( (id * v_mult) & v_mask) >> 5   ) % 32 + 1)::INT, 1) ||
    substr(v_alphabet, ((( (id * v_mult) & v_mask) >> 10  ) % 32 + 1)::INT, 1) ||
    substr(v_alphabet, ((( (id * v_mult) & v_mask) >> 15  ) % 32 + 1)::INT, 1) ||
    substr(v_alphabet, ((( (id * v_mult) & v_mask) >> 20  ) % 32 + 1)::INT, 1) ||
    substr(v_alphabet, ((( (id * v_mult) & v_mask) >> 25  ) % 32 + 1)::INT, 1)
    );
END $$;