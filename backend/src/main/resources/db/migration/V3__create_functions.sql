-- V3__create_functions.sql
-- Database functions for BloodLink

-- Function to get blood stock for a specific bank and blood type
CREATE OR REPLACE FUNCTION get_blood_stock(
    p_bank_id BIGINT, 
    p_blood_group VARCHAR, 
    p_rh_factor VARCHAR
) RETURNS DOUBLE PRECISION AS $$
DECLARE
    blood_count DOUBLE PRECISION;
BEGIN
    SELECT COALESCE(total_quantity, 0)
    INTO blood_count
    FROM blood_reserves br
    WHERE br.bank_id = p_bank_id
      AND br.blood_group = p_blood_group
      AND br.rh_factor = p_rh_factor;

    RETURN COALESCE(blood_count, 0);
END;
$$ LANGUAGE plpgsql;

-- Function to get total blood quantity in a bank
CREATE OR REPLACE FUNCTION get_total_blood_in_bank(p_bank_id BIGINT)
RETURNS DOUBLE PRECISION AS $$
DECLARE
    total_blood DOUBLE PRECISION;
BEGIN
    SELECT COALESCE(SUM(total_quantity), 0)
    INTO total_blood
    FROM blood_reserves br
    WHERE br.bank_id = p_bank_id;

    RETURN total_blood;
END;
$$ LANGUAGE plpgsql;

-- Function to get count of pending requests for a bank
CREATE OR REPLACE FUNCTION get_pending_requests_count(p_bank_id BIGINT)
RETURNS INTEGER AS $$
DECLARE
    request_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO request_count
    FROM requests_to_bank rtb
    WHERE rtb.blood_bank_id = p_bank_id
      AND rtb.status = 'PENDING';

    RETURN request_count;
END;
$$ LANGUAGE plpgsql;

-- Procedure to recalculate blood reserves from blood_units
CREATE OR REPLACE PROCEDURE recalculate_blood_reserves()
LANGUAGE plpgsql AS $$
BEGIN
    -- Clear and recalculate all reserves
    DELETE FROM blood_reserves;
    
    INSERT INTO blood_reserves (bank_id, blood_group, rh_factor, total_quantity)
    SELECT 
        blood_bank_id,
        blood_group,
        rh_factor,
        SUM(volume)
    FROM blood_units
    WHERE blood_bank_id IS NOT NULL
    GROUP BY blood_bank_id, blood_group, rh_factor;
END;
$$;
