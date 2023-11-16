-- Drop the auto-increment constraint
ALTER TABLE vertex ALTER COLUMN vertex_id DROP DEFAULT;

-- If needed,  drop the SERIAL type (optional)
--ALTER TABLE vertex ALTER COLUMN vertex_id TYPE bigint;

-- to Change the data type to double precision
ALTER TABLE vertex ALTER COLUMN vertex_id TYPE double precision;
