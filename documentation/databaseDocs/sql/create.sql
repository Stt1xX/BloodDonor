CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    surname TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    role TEXT NOT NULL CHECK (role IN ('medical_employee', 'bank_employee', 'admin'))
);

CREATE TABLE medical_institutions (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    address TEXT NOT NULL
);

CREATE TABLE medical_employees (
    user_id INTEGER PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    institution_id INTEGER REFERENCES medical_institutions(id) ON DELETE CASCADE,
    post TEXT NOT NULL
);

CREATE TABLE blood_banks (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    address TEXT NOT NULL,
    work_time TEXT NOT NULL
);

CREATE TABLE bank_employees (
    user_id INTEGER PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    bank_id INTEGER REFERENCES blood_banks(id) ON DELETE CASCADE,
    post TEXT NOT NULL
);

CREATE TABLE blood_requests (
    id SERIAL PRIMARY KEY,
    blood_type TEXT CHECK (blood_type IN ('A', 'B', 'AB', 'O')),
    rh_factor TEXT CHECK (rh_factor IN ('+', '-')),
    status TEXT CHECK (status IN ('pending', 'approved', 'rejected', 'completed')),
    quantity INTEGER CHECK (quantity > 0),
    priority TEXT CHECK (priority IN ('low', 'medium', 'high')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    approved_at TIMESTAMP NULL,
    rejected_at TIMESTAMP NULL,
    completed_at TIMESTAMP NULL,
    rejection_reason TEXT NULL
);

CREATE TABLE blood_units (
    id SERIAL PRIMARY KEY,
    bank_id INTEGER REFERENCES blood_banks(id) ON DELETE CASCADE,
    blood_type TEXT CHECK (blood_type IN ('A', 'B', 'AB', 'O')),
    rh_factor TEXT CHECK (rh_factor IN ('+', '-')),
    expiration_date DATE NOT NULL,
    quantity INTEGER CHECK (quantity > 0)
);

CREATE TABLE blood_inventories (
    bank_id INTEGER REFERENCES blood_banks(id) ON DELETE CASCADE,
    blood_type TEXT CHECK (blood_type IN ('A', 'B', 'AB', 'O')),
    rh_factor TEXT CHECK (rh_factor IN ('+', '-')),
    total_quantity INTEGER CHECK (total_quantity > 0),
    PRIMARY KEY (bank_id, blood_type, rh_factor)
);

CREATE TABLE bank_request (
    bank_id INTEGER REFERENCES blood_banks(id) ON DELETE CASCADE,
    blood_request_id INTEGER REFERENCES blood_requests(id) ON DELETE CASCADE,
    PRIMARY KEY (bank_id, blood_request_id)
);

CREATE TABLE registration_requests (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    surname TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    role TEXT CHECK (role IN ('medical_employee', 'bank_employee', 'admin')),
    institution_id INTEGER REFERENCES medical_institutions(id) ON DELETE CASCADE,
    bank_id INTEGER REFERENCES blood_banks(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE change_status_notifications (
    id SERIAL PRIMARY KEY,
    blood_request_id INTEGER REFERENCES blood_requests(id) ON DELETE CASCADE,
    medical_employee_id INTEGER REFERENCES medical_employees(user_id) ON DELETE CASCADE,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    new_status TEXT CHECK (new_status IN ('approved', 'rejected'))
);

CREATE TABLE expiration_notifications (
    id SERIAL PRIMARY KEY,
    blood_unit_id INTEGER REFERENCES blood_units(id) ON DELETE CASCADE,
    bank_employee_id INTEGER REFERENCES bank_employees(user_id) ON DELETE CASCADE,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE new_request_notifications (
    id SERIAL PRIMARY KEY,
    blood_request_id INTEGER REFERENCES blood_requests(id) ON DELETE CASCADE,
    bank_employee_id INTEGER REFERENCES bank_employees(user_id) ON DELETE CASCADE,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
