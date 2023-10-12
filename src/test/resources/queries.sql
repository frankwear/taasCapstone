-- querie to . create sample table while establishing DB connection- ticket 110
CREATE TABLE jsontable (
                           id SERIAL PRIMARY KEY,
                           origin_latitude DOUBLE PRECISION,
                           origin_longitude DOUBLE PRECISION,
                           destination_latitude DOUBLE PRECISION,
                           destination_longitude DOUBLE PRECISION,
                           mode VARCHAR(255),
                           distance INTEGER,
                           duration INTEGER
);
--queries created as per ERD diagram - ticket (28)-ticket -125
