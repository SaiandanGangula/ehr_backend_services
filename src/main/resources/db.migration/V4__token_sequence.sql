CREATE TABLE token_sequence (
    sequence_id SERIAL PRIMARY KEY,
    date_of_issue DATE UNIQUE NOT NULL,
    last_token_number INT NOT NULL DEFAULT 0
);