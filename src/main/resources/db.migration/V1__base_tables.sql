CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TYPE title_enum         AS ENUM ('Mr.', 'Mrs.', 'Ms.', 'Dr.', 'Master', 'Miss', 'Other');
CREATE TYPE identifier_enum    AS ENUM ('ABHA', 'Aadhar', 'Passport', 'Driving_License', 'PAN');
CREATE TYPE gender_enum        AS ENUM ('Male', 'Female', 'Other');

CREATE TABLE patients (
  patient_id       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  facility_id      UUID NOT NULL,
  identifier_type  identifier_enum NOT NULL,
  identifier_number VARCHAR(20) UNIQUE NOT NULL,
  title            title_enum,
  first_name       VARCHAR(50),
  middle_name      VARCHAR(50),
  last_name        VARCHAR(50),
  date_of_birth    DATE,
  gender           gender_enum,
  registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TYPE contact_mode_enum  AS ENUM ('Phone', 'Email', 'None');
CREATE TYPE phone_pref_enum    AS ENUM ('Call', 'SMS', 'WhatsApp');

CREATE TABLE patient_contacts (
  contact_id  SERIAL PRIMARY KEY,
  patient_id  UUID REFERENCES patients(patient_id) ON DELETE CASCADE,
  phone_number VARCHAR(15) NOT NULL,
  email       VARCHAR(100),
  preferred_contact_mode contact_mode_enum,
  phone_contact_preference phone_pref_enum,
  consent_to_share BOOLEAN DEFAULT FALSE
);

CREATE TABLE emergency_contacts (
  emergency_contact_id SERIAL PRIMARY KEY,
  patient_id  UUID REFERENCES patients(patient_id) ON DELETE CASCADE,
  contact_name VARCHAR(50),
  relationship VARCHAR(50),
  phone_number VARCHAR(15)
);

-- V2__dynamic_metadata.sql
CREATE TYPE field_type_enum AS ENUM ('TEXT','DATE','SELECT','NUMBER','BOOLEAN');

CREATE TABLE registration_field_config (
  config_id   SERIAL PRIMARY KEY,
  field_name  VARCHAR(100) UNIQUE NOT NULL,
  label       VARCHAR(100) NOT NULL,
  field_type  field_type_enum NOT NULL,
  required    BOOLEAN NOT NULL DEFAULT FALSE,
  visible     BOOLEAN NOT NULL DEFAULT TRUE,
  sort_order  INT NOT NULL DEFAULT 0
);

CREATE TABLE registration_field_option (
  option_id  SERIAL PRIMARY KEY,
  field_name VARCHAR(100) REFERENCES registration_field_config(field_name) ON DELETE CASCADE,
  value      VARCHAR(100) NOT NULL,
  display    VARCHAR(100) NOT NULL,
  sort_order INT NOT NULL DEFAULT 0
);