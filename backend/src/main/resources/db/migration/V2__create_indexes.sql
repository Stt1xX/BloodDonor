-- V2__create_indexes.sql
-- Performance indexes for BloodLink database

-- =====================================================
-- BLOOD_UNITS indexes
-- =====================================================
CREATE INDEX IF NOT EXISTS idx_blood_units_blood_bank_id 
    ON blood_units (blood_bank_id);

CREATE INDEX IF NOT EXISTS idx_blood_units_blood_group 
    ON blood_units (blood_group);

CREATE INDEX IF NOT EXISTS idx_blood_units_rh_factor 
    ON blood_units (rh_factor);

CREATE INDEX IF NOT EXISTS idx_blood_units_expiration_date 
    ON blood_units (expiration_date);

CREATE INDEX IF NOT EXISTS idx_blood_units_blood_type 
    ON blood_units (blood_group, rh_factor);

CREATE INDEX IF NOT EXISTS idx_blood_units_bank_expiration 
    ON blood_units (blood_bank_id, expiration_date);

-- =====================================================
-- BLOOD_REQUESTS indexes
-- =====================================================
CREATE INDEX IF NOT EXISTS idx_blood_requests_creator_id 
    ON blood_requests (creator_id);

CREATE INDEX IF NOT EXISTS idx_blood_requests_medical_institution_id 
    ON blood_requests (medical_institution_id);

CREATE INDEX IF NOT EXISTS idx_blood_requests_blood_group 
    ON blood_requests (blood_group);

CREATE INDEX IF NOT EXISTS idx_blood_requests_rh_factor 
    ON blood_requests (rh_factor);

CREATE INDEX IF NOT EXISTS idx_blood_requests_created_at 
    ON blood_requests (created_at DESC);

CREATE INDEX IF NOT EXISTS idx_blood_requests_is_emergency 
    ON blood_requests (is_emergency) WHERE is_emergency = TRUE;

-- =====================================================
-- REQUESTS_TO_BANK indexes
-- =====================================================
CREATE INDEX IF NOT EXISTS idx_requests_to_bank_request_id 
    ON requests_to_bank (request_id);

CREATE INDEX IF NOT EXISTS idx_requests_to_bank_blood_bank_id 
    ON requests_to_bank (blood_bank_id);

CREATE INDEX IF NOT EXISTS idx_requests_to_bank_banker_id 
    ON requests_to_bank (banker_id);

CREATE INDEX IF NOT EXISTS idx_requests_to_bank_status 
    ON requests_to_bank (status);

-- =====================================================
-- USERS indexes
-- =====================================================
CREATE INDEX IF NOT EXISTS idx_users_organization_id 
    ON users (organization_id);

CREATE INDEX IF NOT EXISTS idx_users_role 
    ON users (role);

CREATE INDEX IF NOT EXISTS idx_users_email 
    ON users (email) WHERE is_deleted = FALSE;

CREATE INDEX IF NOT EXISTS idx_users_role_org 
    ON users (role, organization_id) WHERE is_deleted = FALSE;

-- =====================================================
-- BLOOD_RESERVES indexes
-- =====================================================
CREATE INDEX IF NOT EXISTS idx_blood_reserves_blood_group_rh 
    ON blood_reserves (blood_group, rh_factor, total_quantity);

-- =====================================================
-- NOTIFICATIONS indexes
-- =====================================================
CREATE INDEX IF NOT EXISTS idx_notifications_user_id 
    ON notifications (user_id);

CREATE INDEX IF NOT EXISTS idx_notifications_created_at 
    ON notifications (created_at DESC);

CREATE INDEX IF NOT EXISTS idx_notifications_is_read 
    ON notifications (is_read);

CREATE INDEX IF NOT EXISTS idx_notifications_user_read 
    ON notifications (user_id, is_read);

-- =====================================================
-- REGISTRATION_REQUESTS indexes
-- =====================================================
CREATE INDEX IF NOT EXISTS idx_registration_requests_organization_id 
    ON registration_requests (organization_id);

CREATE INDEX IF NOT EXISTS idx_registration_requests_email 
    ON registration_requests (email);

-- =====================================================
-- ORGANIZATIONS indexes
-- =====================================================
CREATE INDEX IF NOT EXISTS idx_organizations_type 
    ON organizations (type);
