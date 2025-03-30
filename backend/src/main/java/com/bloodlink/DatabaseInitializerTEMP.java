package com.bloodlink;

import jakarta.annotation.PostConstruct;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializerTEMP {
    private final JdbcTemplate jdbcTemplate;

    public DatabaseInitializerTEMP(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void init() {
        createTriggerFunction();
        createTrigger();
    }

    private void createTriggerFunction() {
        String sql = """
            CREATE OR REPLACE FUNCTION update_blood_reserves()
            RETURNS TRIGGER AS $$
            BEGIN
                IF TG_OP = 'INSERT' THEN
                    INSERT INTO blood_reserves (bank_id, blood_group, rh_factor, total_quantity)
                    VALUES (NEW.blood_bank_id, NEW.blood_group, NEW.rh_factor, NEW.volume)
                    ON CONFLICT (bank_id, blood_group, rh_factor)
                    DO UPDATE SET total_quantity = blood_reserves.total_quantity + NEW.volume;

                ELSIF TG_OP = 'DELETE' THEN
                    UPDATE blood_reserves
                    SET total_quantity = GREATEST(0, total_quantity - OLD.volume)
                    WHERE bank_id = OLD.blood_bank_id
                      AND blood_group = OLD.blood_group
                      AND rh_factor = OLD.rh_factor;

                ELSIF TG_OP = 'UPDATE' THEN
                    UPDATE blood_reserves
                    SET total_quantity = GREATEST(0, total_quantity - OLD.volume + NEW.volume)
                    WHERE bank_id = OLD.blood_bank_id
                      AND blood_group = OLD.blood_group
                      AND rh_factor = OLD.rh_factor;
                END IF;

                RETURN NEW;
            END;
            $$ LANGUAGE plpgsql;
            """;

        jdbcTemplate.execute(sql);
    }

    private void createTrigger() {
        String sql = """
            CREATE OR REPLACE TRIGGER trg_update_inventory
            AFTER INSERT OR UPDATE OR DELETE ON blood_units
            FOR EACH ROW EXECUTE FUNCTION update_blood_reserves();
            """;

        jdbcTemplate.execute(sql);
    }
}