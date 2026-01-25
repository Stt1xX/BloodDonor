-- V1__create_tables.sql
-- BloodLink Database Schema - Tables Creation

-- Organizations table (blood banks and medical institutions)
CREATE TABLE IF NOT EXISTS organizations (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    address VARCHAR(255),
    phone VARCHAR(255),
    type VARCHAR(50) NOT NULL CHECK (type IN ('BLOOD_BANK', 'MEDICAL_INSTITUTION')),
    hours_from INTEGER,
    hours_to INTEGER,
    minutes_from INTEGER,
    minutes_to INTEGER
);

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    post VARCHAR(255),
    role VARCHAR(50) NOT NULL CHECK (role IN ('ADMIN', 'MEDICAL_EMPLOYEE', 'BANK_EMPLOYEE')),
    organization_id BIGINT REFERENCES organizations(id) ON DELETE CASCADE,
    is_deleted BOOLEAN DEFAULT FALSE
);

-- Blood requests table
CREATE TABLE IF NOT EXISTS blood_requests (
    id BIGSERIAL PRIMARY KEY,
    blood_group VARCHAR(10) CHECK (blood_group IN ('A', 'B', 'AB', 'O')),
    rh_factor VARCHAR(20) CHECK (rh_factor IN ('POSITIVE', 'NEGATIVE')),
    volume_needed DOUBLE PRECISION,
    description VARCHAR(255),
    is_emergency BOOLEAN DEFAULT FALSE,
    creator_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
    medical_institution_id BIGINT REFERENCES organizations(id) ON DELETE CASCADE,
    created_at TIMESTAMP(6) WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(6) WITHOUT TIME ZONE
);

-- Blood units table
CREATE TABLE IF NOT EXISTS blood_units (
    id BIGSERIAL PRIMARY KEY,
    blood_group VARCHAR(10),
    rh_factor VARCHAR(20),
    volume DOUBLE PRECISION,
    created_at DATE,
    expiration_date DATE,
    blood_bank_id BIGINT REFERENCES organizations(id) ON DELETE CASCADE
);

-- Blood reserves table (aggregated inventory)
CREATE TABLE IF NOT EXISTS blood_reserves (
    bank_id BIGINT NOT NULL REFERENCES organizations(id),
    blood_group VARCHAR(255) NOT NULL,
    rh_factor VARCHAR(255) NOT NULL,
    total_quantity DOUBLE PRECISION DEFAULT 0,
    PRIMARY KEY (bank_id, blood_group, rh_factor)
);

-- Requests to bank (junction table for blood request distribution)
CREATE TABLE IF NOT EXISTS requests_to_bank (
    id BIGSERIAL PRIMARY KEY,
    request_id BIGINT REFERENCES blood_requests(id) ON DELETE CASCADE,
    blood_bank_id BIGINT REFERENCES organizations(id) ON DELETE CASCADE,
    banker_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
    status VARCHAR(50) CHECK (status IN ('PENDING', 'REJECTED', 'DELETED', 'COMPLETED')),
    rejection_reason VARCHAR(255),
    created_at TIMESTAMP(6) WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(6) WITHOUT TIME ZONE
);

-- Registration requests table
CREATE TABLE IF NOT EXISTS registration_requests (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    post VARCHAR(255),
    role VARCHAR(50) CHECK (role IN ('ADMIN', 'MEDICAL_EMPLOYEE', 'BANK_EMPLOYEE')),
    organization_id BIGINT UNIQUE REFERENCES organizations(id) ON DELETE CASCADE,
    created_at TIMESTAMP(6) WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Notifications table
CREATE TABLE IF NOT EXISTS notifications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    notification TEXT NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
