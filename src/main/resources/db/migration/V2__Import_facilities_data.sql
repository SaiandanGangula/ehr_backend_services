COPY facility(
  hospital_id, facility_name, facility_type, block, phc_chc_name,
  location, officer_in_charge, designation, contact_number,
  official_email, network_id, bed_strength, patient_types, notes, equipments
)
FROM 'D:/ehr-backend-server/src/main/resources/data/facilities.csv'
WITH (FORMAT csv, HEADER true);
