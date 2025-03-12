-- добавление данных в total_sum для каждого банка при insert нового blood_item
CREATE OR REPLACE FUNCTION update_inventory_on_insert()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO blood_inventories (bank_id, blood_type, rh_factor, total_quantity)
    VALUES (NEW.bank_id, NEW.blood_type, NEW.rh_factor, NEW.quantity)
    ON CONFLICT (bank_id, blood_type, rh_factor)
    DO UPDATE SET total_quantity = blood_inventories.total_quantity + NEW.quantity;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER trigger_update_inventory
AFTER INSERT ON blood_units
FOR EACH ROW
EXECUTE FUNCTION update_inventory_on_insert();

-- тоже самое для удаления
CREATE OR REPLACE FUNCTION update_inventory_on_delete()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE blood_inventories
    SET total_quantity = GREATEST(total_quantity - OLD.quantity, 0)
    WHERE bank_id = OLD.bank_id
      AND blood_type = OLD.blood_type
      AND rh_factor = OLD.rh_factor;

    RETURN OLD;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER trigger_update_inventory_on_delete
AFTER DELETE ON blood_units
FOR EACH ROW
EXECUTE FUNCTION update_inventory_on_delete();

-- Проверка на наличие крови для запроса
CREATE OR REPLACE FUNCTION check_blood_availability()
RETURNS TRIGGER AS $$
DECLARE
    available_quantity INTEGER;
BEGIN
    SELECT total_quantity INTO available_quantity
    FROM blood_inventories
    WHERE bank_id = NEW.bank_id
      AND blood_type = NEW.blood_type
      AND rh_factor = NEW.rh_factor;
    IF available_quantity IS NULL OR available_quantity < NEW.quantity THEN
        RAISE EXCEPTION 'Недостаточное количество крови в банке!';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER trigger_check_blood_availability
BEFORE INSERT ON blood_requests
FOR EACH ROW
EXECUTE FUNCTION check_blood_availability();
