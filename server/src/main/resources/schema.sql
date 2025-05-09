CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(512) NOT NULL
    );

CREATE TABLE IF NOT EXISTS item_requests (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    description VARCHAR(512) NOT NULL,
    user_id BIGINT NOT NULL REFERENCES users(id),
    created TIMESTAMP NOT NULL
    );

CREATE TABLE IF NOT EXISTS items (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    owner_id BIGINT NOT NULL REFERENCES users(id),
    name VARCHAR(255) NOT NULL,
    description VARCHAR(512) NOT NULL,
    available BOOL,
    request_id BIGINT REFERENCES item_requests(id)
    );

CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    item_id BIGINT NOT NULL REFERENCES items(id),
    booker_id BIGINT NOT NULL REFERENCES users(id),
    status VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS comments (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    text VARCHAR(512) NOT NULL,
    item_id BIGINT NOT NULL REFERENCES items(id),
    user_id BIGINT NOT NULL REFERENCES users(id),
    created TIMESTAMP NOT NULL
);

