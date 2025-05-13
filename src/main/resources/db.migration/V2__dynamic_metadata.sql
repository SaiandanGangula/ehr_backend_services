CREATE TYPE field_type_enum AS ENUM ('TEXT','DATE','SELECT','NUMBER','BOOLEAN');

CREATE TABLE registration_field_config (
  config_id SERIAL PRIMARY KEY,
  field_name VARCHAR(100) UNIQUE NOT NULL,
  label      VARCHAR(100) NOT NULL,
  field_type field_type_enum NOT NULL,
  required   BOOLEAN NOT NULL DEFAULT FALSE,
  visible    BOOLEAN NOT NULL DEFAULT TRUE,
  sort_order INT NOT NULL DEFAULT 0
);

CREATE TABLE registration_field_option (
  option_id  SERIAL PRIMARY KEY,
  field_name VARCHAR(100) REFERENCES registration_field_config(field_name) ON DELETE CASCADE,
  value      VARCHAR(100) NOT NULL,
  display    VARCHAR(100) NOT NULL,
  sort_order INT NOT NULL DEFAULT 0
);