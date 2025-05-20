-- Ensure pgcrypto is enabled
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Ensure 'id' defaults to a UUID if not supplied
ALTER TABLE lookup_values
ALTER COLUMN id SET DEFAULT gen_random_uuid();

-- Now import the data (without 'id' column)
COPY lookup_values (
  category, code, display_name, sort_order, active, created_at, updated_at
)
FROM 'D:/ehr-backend-server/src/main/resources/data/lookup_values.csv'
WITH (FORMAT csv, HEADER true);
