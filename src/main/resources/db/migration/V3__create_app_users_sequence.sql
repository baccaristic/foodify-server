DO $$
DECLARE
    current_max BIGINT;
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.sequences WHERE sequence_schema = 'public' AND sequence_name = 'app_users_seq'
    ) THEN
        EXECUTE 'CREATE SEQUENCE app_users_seq START WITH 1 INCREMENT BY 1';
    END IF;

    IF EXISTS (
        SELECT 1 FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'app_users'
    ) THEN
        SELECT COALESCE(MAX(id), 0) INTO current_max FROM app_users;

        IF current_max = 0 THEN
            PERFORM setval('app_users_seq', 1, false);
        ELSE
            PERFORM setval('app_users_seq', current_max, true);
        END IF;

        EXECUTE 'ALTER TABLE app_users ALTER COLUMN id SET DEFAULT nextval(''app_users_seq'')';
    END IF;
END
$$;
