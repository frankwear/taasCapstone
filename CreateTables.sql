CREATE TABLE vertex (
    vertex_id SERIAL PRIMARY KEY,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    description VARCHAR(255) NOT NULL,
    thirdparty_id VARCHAR(255) NOT NULL,
    last_updated TIMESTAMP NOT NULL,
    source VARCHAR(255) NOT NULL,
    private BOOLEAN NOT NULL,
    range DOUBLE PRECISION NOT NULL
);

CREATE TABLE edge (
    edge_id SERIAL PRIMARY KEY,
    start_vertex_id INT NOT NULL,
    end_vertex_id INT NOT NULL,
    cost NUMERIC(10,2) NOT NULL,
    distance NUMERIC(10,2) NOT NULL,
    duration INT NOT NULL,
    mode VARCHAR(255) NOT NULL
);

CREATE TABLE user_details (
    user_id SERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE user_vertex (
    user_vertex_id SERIAL PRIMARY KEY,
    vertex_id INT NOT NULL REFERENCES vertex(vertex_id),
    user_id INT NOT NULL REFERENCES user_details(user_id)
);


