CREATE OR REPLACE FUNCTION update_blood_inventory()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        INSERT INTO blood_inventory (bank_id, blood_type, rh_factor, total_quantity)
        VALUES (NEW.bank_id, NEW.blood_type, NEW.rh_factor, NEW.volume)
        ON CONFLICT (bank_id, blood_type, rh_factor)
        DO UPDATE SET total_quantity = blood_inventory.total_quantity + NEW.volume;

    ELSIF TG_OP = 'DELETE' THEN
        UPDATE blood_inventory
        SET total_quantity = GREATEST(0, total_quantity - OLD.volume)
        WHERE bank_id = OLD.bank_id
          AND blood_type = OLD.blood_type
          AND rh_factor = OLD.rh_factor;

    ELSIF TG_OP = 'UPDATE' THEN
    UPDATE blood_inventory
    SET total_quantity = GREATEST(0, total_quantity - OLD.volume + NEW.volume)
    WHERE bank_id = OLD.bank_id
      AND blood_type = OLD.blood_type
      AND rh_factor = OLD.rh_factor;
    END IF;

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_inventory
    AFTER INSERT OR UPDATE OR DELETE ON blood_units
    FOR EACH ROW EXECUTE FUNCTION update_blood_inventory();
