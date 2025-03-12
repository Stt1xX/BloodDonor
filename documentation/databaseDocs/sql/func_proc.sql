-- текущий запас крови определенного банка определенной группы и rh фактора
CREATE OR REPLACE FUNCTION get_blood_stock(bank_id INT, blood_type TEXT, rh_factor TEXT)
RETURNS INT AS $$
DECLARE
    blood_count INT;
BEGIN
    SELECT COALESCE(total_quantity, 0)
    INTO blood_count
    FROM blood_inventories bi
    WHERE bi.bank_id = get_blood_stock.bank_id
      AND bi.blood_type = get_blood_stock.blood_type
      AND bi.rh_factor = get_blood_stock.rh_factor;

    RETURN blood_count;
END;
$$ LANGUAGE plpgsql;
-- использование
SELECT get_blood_stock(3, 'A', '+');

-- текущий запас всех видов крови для банка
CREATE OR REPLACE FUNCTION get_total_blood_in_bank(bank_id INT)
RETURNS INT AS $$
DECLARE
    total_blood INT;
BEGIN
    SELECT COALESCE(SUM(total_quantity), 0)
    INTO total_blood
    FROM blood_inventories bi
    WHERE bi.bank_id = get_total_blood_in_bank.bank_id;

    RETURN total_blood;
END;
$$ LANGUAGE plpgsql;
-- использование
SELECT get_total_blood_in_bank(2);

-- количество заявок в ожидании обработки для банка
CREATE OR REPLACE FUNCTION get_pending_requests_count(bank_id INT)
RETURNS INT AS $$
DECLARE
    request_count INT;
BEGIN
    SELECT COUNT(*) INTO request_count
    FROM blood_requests br
    JOIN bank_request brq ON br.id = brq.blood_request_id
    WHERE brq.bank_id = get_pending_requests_count.bank_id
    AND br.status = 'pending';

    RETURN request_count;
END;
$$ LANGUAGE plpgsql;
-- использование
SELECT get_pending_requests_count(2);


--ПРОЦЕДУРЫ

-- добавляет кровь в таблицу blood_inventories
CREATE OR REPLACE PROCEDURE add_blood_to_inventory(
    p_bank_id INT, 
    p_blood_type TEXT, 
    p_rh_factor TEXT, 
    p_quantity INT
)
LANGUAGE plpgsql AS $$
BEGIN
    INSERT INTO blood_inventories (bank_id, blood_type, rh_factor, total_quantity)
    VALUES (p_bank_id, p_blood_type, p_rh_factor, p_quantity)
    ON CONFLICT (bank_id, blood_type, rh_factor)
    DO UPDATE SET total_quantity = blood_inventories.total_quantity + p_quantity;
END;
$$;
-- использование
CALL add_blood_to_inventory(1, 'O', '+', 10);

-- пересчет blood_inventory
CREATE OR REPLACE PROCEDURE recalculate_blood_inventory()
LANGUAGE plpgsql
AS $$
BEGIN
    -- Обновляем существующие записи
    UPDATE blood_inventories bi
    SET total_quantity = COALESCE((
        SELECT SUM(quantity)
        FROM blood_units bu
        WHERE bu.bank_id = bi.bank_id 
          AND bu.blood_type = bi.blood_type 
          AND bu.rh_factor = bi.rh_factor
    ), 0); -- Если нет записей, ставим 0
END;
$$;
-- использование
CALL recalculate_blood_inventory();
