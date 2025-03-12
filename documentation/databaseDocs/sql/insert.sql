-- Генерация пользователей (1000 записей)
INSERT INTO users (name, surname, email, password, role)
SELECT 
    'Name' || id, 
    'Surname' || id, 
    'user' || id || '@example.com', 
    'password123', 
    CASE WHEN id % 3 = 0 THEN 'medical_employee' 
         WHEN id % 3 = 1 THEN 'bank_employee' 
         ELSE 'admin' 
    END
FROM generate_series(1, 1000) AS id;

-- Генерация медицинских учреждений (100 записей)
INSERT INTO medical_institutions (name, address)
SELECT 'Hospital ' || id, 'Medicinskaya st., ' || (id % 50 + 1)
FROM generate_series(1, 100) AS id;

-- Генерация банков крови (50 записей)
INSERT INTO blood_banks (name, address, work_time)
SELECT 'Blood bank ' || id, 'Medicinskaya st., ' || (id % 30 + 1), '08:00-18:00'
FROM generate_series(1, 50) AS id;

-- Генерация сотрудников мед учреждения (500 записей)
INSERT INTO medical_employees (user_id, institution_id, post)
SELECT id, (id % 100) + 1, 'Doctor'
FROM generate_series(1, 500) AS id;

-- Генерация сотрудников банка (500 записей)
INSERT INTO bank_employees (user_id, bank_id, post)
SELECT id, (id % 50) + 1, 'Laboratory assistant'
FROM generate_series(501, 1000) AS id;

-- Генерация единиц крови (10 000 записей)
INSERT INTO blood_units (bank_id, blood_type, rh_factor, expiration_date, quantity)
SELECT 
    (id % 50) + 1, 
    CASE WHEN id % 4 = 0 THEN 'A' WHEN id % 4 = 1 THEN 'B' WHEN id % 4 = 2 THEN 'AB' ELSE 'O' END,
    CASE WHEN id % 2 = 0 THEN '+' ELSE '-' END,
    NOW() - INTERVAL '1 day' * (id % 365),
    (id % 5) + 1
FROM generate_series(1, 10000) AS id;

-- Генерация запросов на кровь (5000 записей)
INSERT INTO blood_requests (blood_type, rh_factor, status, quantity, priority, created_at)
SELECT 
    CASE WHEN id % 4 = 0 THEN 'A' WHEN id % 4 = 1 THEN 'B' WHEN id % 4 = 2 THEN 'AB' ELSE 'O' END,
    CASE WHEN id % 2 = 0 THEN '+' ELSE '-' END,
    CASE WHEN id % 3 = 0 THEN 'pending' WHEN id % 3 = 1 THEN 'approved' ELSE 'rejected' END,
    (id % 3) + 1,
    CASE WHEN id % 3 = 0 THEN 'low' WHEN id % 3 = 1 THEN 'medium' ELSE 'high' END,
    NOW() - INTERVAL '1 day' * (id % 30)
FROM generate_series(1, 5000) AS id;

-- Добавляем запросы на регистрацию (200 записей)
INSERT INTO registration_requests (name, surname, email, password, role, institution_id, created_at)
SELECT 
    'Name' || id, 
    'Surname' || id,
    'request' || id || '@example.com', 
    'password123', 
    CASE WHEN id % 3 = 0 THEN 'medical_employee' 
         WHEN id % 3 = 1 THEN 'bank_employee' 
         ELSE 'admin' 
    END, 
    (id % 100) + 1, 
    NOW() - INTERVAL '1 day' * (id % 30)
FROM generate_series(1, 200) AS id;

-- Добавляем уведомления о статусе (500 записей)
INSERT INTO change_status_notifications (blood_request_id, medical_employee_id, is_read, created_at, new_status)
SELECT 
    (id % 5000) + 1, 
    (id % 500) + 1, 
    FALSE, 
    NOW() - INTERVAL '1 day' * (id % 30),
    CASE WHEN id % 3 = 0 THEN 'approved' ELSE 'rejected' END
FROM generate_series(1, 500) AS id;

-- Добавляем уведомления об истечении срока годности (500 записей)
INSERT INTO expiration_notifications (blood_unit_id, bank_employee_id, is_read, created_at)
SELECT 
    (id % 10000) + 1, 
    (id % 500) + 501, 
    FALSE, 
    NOW() - INTERVAL '1 day' * (id % 30)
FROM generate_series(1, 500) AS id;

-- Добавляем уведомления о новых запросах (500 записей)
INSERT INTO new_request_notifications (blood_request_id, bank_employee_id, is_read, created_at)
SELECT 
    (id % 5000) + 1, 
    (id % 500) + 501, 
    FALSE, 
    NOW() - INTERVAL '1 day' * (id % 30)
FROM generate_series(1, 500) AS id;

-- Добавляем заявки на выдачу крови (1000 записей)
INSERT INTO bank_request (bank_id, blood_request_id)
SELECT 
    (id % 50) + 1, 
    (id % 5000) + 1
FROM generate_series(1, 1000) AS id;

-- Добавляем данные в blood_inventories (50 банков × 8 типов крови = 400 записей)
INSERT INTO blood_inventories (bank_id, blood_type, rh_factor, total_quantity)
SELECT 
    bank_id,
    blood_type,
    rh_factor,
    (random() * 50 + 1)::int -- случайное количество от 1 до 50
FROM (
    SELECT DISTINCT ON (bank_id, blood_type, rh_factor)
        bank_id,
        blood_type,
        rh_factor
    FROM (
        SELECT 
            (b % 50) + 1 AS bank_id, 
            blood_type, 
            rh_factor
        FROM generate_series(1, 50) b
        CROSS JOIN (VALUES ('A', '+'), ('A', '-'), ('B', '+'), ('B', '-'), ('AB', '+'), ('AB', '-'), ('O', '+'), ('O', '-')) AS blood_types(blood_type, rh_factor)
    ) AS unique_combinations
) AS final_selection;
