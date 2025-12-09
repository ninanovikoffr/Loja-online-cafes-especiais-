-- Flyway migration: synchronize produtos id sequence with current max(id_produto)
-- This ensures the sequence next value won't collide with existing primary keys.

SELECT pg_get_serial_sequence('produtos','id_produto') as seq_name;

DO $$
DECLARE
    seq_name text := pg_get_serial_sequence('produtos','id_produto');
    max_id bigint;
BEGIN
    IF seq_name IS NULL THEN
        RAISE NOTICE 'Sequence not found for produtos.id_produto';
        RETURN;
    END IF;

    SELECT COALESCE(MAX(id_produto), 0) INTO max_id FROM produtos;

    IF max_id IS NULL THEN
        max_id := 0;
    END IF;

    -- set sequence last_value to max_id (so nextval will return max_id+1)
    EXECUTE format('SELECT setval(%L, %s, true)', seq_name, max_id);

    RAISE NOTICE 'Set % to %', seq_name, max_id;
END $$;
