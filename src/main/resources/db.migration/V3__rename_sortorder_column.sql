DO $$
BEGIN
  IF EXISTS (
       SELECT 1
       FROM   information_schema.columns
       WHERE  table_name = 'registration_field_config'
       AND    column_name = 'sortorder'
  ) THEN
     ALTER TABLE registration_field_config
       RENAME COLUMN sortorder TO sort_order;
  END IF;
END$$;