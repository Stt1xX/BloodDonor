-- V4__create_triggers.sql
-- Triggers for automatic blood reserves management

-- Function to update blood reserves on blood_units changes
CREATE OR REPLACE FUNCTION update_blood_reserves()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        -- Add new blood unit to reserves
        INSERT INTO blood_reserves (bank_id, blood_group, rh_factor, total_quantity)
        VALUES (NEW.blood_bank_id, NEW.blood_group, NEW.rh_factor, NEW.volume)
        ON CONFLICT (bank_id, blood_group, rh_factor)
        DO UPDATE SET total_quantity = blood_reserves.total_quantity + NEW.volume;
        
        RETURN NEW;

    ELSIF TG_OP = 'DELETE' THEN
        -- Remove blood unit from reserves
        UPDATE blood_reserves
        SET total_quantity = GREATEST(0, total_quantity - OLD.volume)
        WHERE bank_id = OLD.blood_bank_id
          AND blood_group = OLD.blood_group
          AND rh_factor = OLD.rh_factor;
        
        RETURN OLD;

    ELSIF TG_OP = 'UPDATE' THEN
        -- Handle blood unit update (subtract old, add new)
        IF OLD.blood_bank_id = NEW.blood_bank_id 
           AND OLD.blood_group = NEW.blood_group 
           AND OLD.rh_factor = NEW.rh_factor THEN
            -- Same bank and blood type, just update quantity
            UPDATE blood_reserves
            SET total_quantity = GREATEST(0, total_quantity - OLD.volume + NEW.volume)
            WHERE bank_id = OLD.blood_bank_id
              AND blood_group = OLD.blood_group
              AND rh_factor = OLD.rh_factor;
        ELSE
            -- Different bank or blood type, subtract from old and add to new
            UPDATE blood_reserves
            SET total_quantity = GREATEST(0, total_quantity - OLD.volume)
            WHERE bank_id = OLD.blood_bank_id
              AND blood_group = OLD.blood_group
              AND rh_factor = OLD.rh_factor;
              
            INSERT INTO blood_reserves (bank_id, blood_group, rh_factor, total_quantity)
            VALUES (NEW.blood_bank_id, NEW.blood_group, NEW.rh_factor, NEW.volume)
            ON CONFLICT (bank_id, blood_group, rh_factor)
            DO UPDATE SET total_quantity = blood_reserves.total_quantity + NEW.volume;
        END IF;
        
        RETURN NEW;
    END IF;
    
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- Trigger to automatically update blood reserves
DROP TRIGGER IF EXISTS trg_update_blood_reserves ON blood_units;
CREATE TRIGGER trg_update_blood_reserves
    AFTER INSERT OR UPDATE OR DELETE ON blood_units
    FOR EACH ROW
    EXECUTE FUNCTION update_blood_reserves();

-- Function to set updated_at timestamp
CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger for blood_requests updated_at
DROP TRIGGER IF EXISTS trg_blood_requests_updated_at ON blood_requests;
CREATE TRIGGER trg_blood_requests_updated_at
    BEFORE UPDATE ON blood_requests
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();

-- Trigger for requests_to_bank updated_at
DROP TRIGGER IF EXISTS trg_requests_to_bank_updated_at ON requests_to_bank;
CREATE TRIGGER trg_requests_to_bank_updated_at
    BEFORE UPDATE ON requests_to_bank
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();
