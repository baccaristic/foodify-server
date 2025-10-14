DO $$
DECLARE
    seq_name TEXT;
    current_max BIGINT;
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.tables
        WHERE table_schema = 'public'
          AND table_name = 'app_users'
    ) THEN
        SELECT pg_get_serial_sequence('app_users', 'id') INTO seq_name;

        IF seq_name IS NOT NULL THEN
            SELECT COALESCE(MAX(id), 0) INTO current_max FROM app_users;

            IF current_max = 0 THEN
                EXECUTE format('SELECT setval(''%s'', 1, false)', seq_name);
            ELSE
                EXECUTE format('SELECT setval(''%s'', %s, true)', seq_name, current_max);
            END IF;
        END IF;
    END IF;
END
$$;
