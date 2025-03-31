INSERT INTO organizations (name, address, phone, type, hours_from, hours_to, minutes_from, minutes_to)
VALUES
    ('Банк крови Альфа', 'ул. Ленина, 10', '123-456-7890', 'BLOOD_BANK', 8, 18, 0, 0),
    ('Банк крови Бета', 'пр. Мира, 25', '987-654-3210', 'BLOOD_BANK', 9, 17, 30, 30),
    ('Городская больница №1', 'ул. Советская, 5', '555-555-5555', 'MEDICAL_INSTITUTION', 7, 19, 0, 0),
    ('Центральный медицинский центр', 'ул. Пушкина, 15', '444-333-2222', 'MEDICAL_INSTITUTION', 8, 20, 15, 45),
    ('Банк крови Гамма', 'ул. Победы, 42', '111-222-3333', 'BLOOD_BANK', 7, 16, 0, 30);

INSERT INTO users (email, name, surname, password, post, role, organization_id, is_deleted)
VALUES
    ('admin@blood.com', 'Иван', 'Иванов', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'Администратор', 'ADMIN', NULL, FALSE),
    ('banker@alpha.com', 'Алиса', 'Смирнова', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'Сотрудник банка', 'BANK_EMPLOYEE', 1, FALSE),
    ('banker@beta.com', 'Дмитрий', 'Петров', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'Сотрудник банка', 'BANK_EMPLOYEE', 2, FALSE),
    ('banker@gamma.com', 'Елена', 'Козлова', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'Сотрудник банка', 'BANK_EMPLOYEE', 5, FALSE),
    ('medic@hospital1.com', 'Борис', 'Сидоров', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'Медработник', 'MEDICAL_EMPLOYEE', 3, FALSE),
    ('medic@clinic.com', 'Мария', 'Федорова', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'Медработник', 'MEDICAL_EMPLOYEE', 4, FALSE);

DO $$
DECLARE
    bank RECORD;
    blood_group TEXT;
    rh_factor TEXT;
    i INTEGER;
    random_volume NUMERIC;
    expiration_days INTEGER;
    created_date DATE;
    expiration_date DATE;
BEGIN

    FOR bank IN SELECT id FROM organizations WHERE type = 'BLOOD_BANK' LOOP
        FOR blood_group IN SELECT unnest(ARRAY['1', '2', '3', '4']) LOOP
            FOR rh_factor IN SELECT unnest(ARRAY['+', '-']) LOOP
                FOR i IN 1..3 LOOP
                    random_volume := 0.5 + floor(random() * 16);
                    expiration_days := 30 + floor(random() * 60);
                    created_date := CURRENT_DATE - (floor(random() * 10) || ' days')::interval;
                    expiration_date := created_date + (expiration_days || ' days')::interval;

                    INSERT INTO blood_units (
                        created_at,
                        expiration_date,
                        volume,
                        blood_bank_id,
                        blood_group,
                        rh_factor
                    ) VALUES (
                         created_date,
                         expiration_date,
                         random_volume,
                         bank.id,
                         blood_group,
                         rh_factor
                     );
                END LOOP;
            END LOOP;
        END LOOP;
    END LOOP;
END $$;