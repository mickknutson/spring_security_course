
-- USERS Schema
CREATE TABLE appUsers (
    id BIGINT IDENTITY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL
);

-- EVENTS Schema
CREATE TABLE events (
    id BIGINT IDENTITY,
    event_date TIMESTAMP NOT NULL,
    summary VARCHAR(100) NOT NULL,
    description VARCHAR(256) NOT NULL,
    owner BIGINT NOT NULL,
    attendee BIGINT NOT NULL,
    FOREIGN KEY(owner) REFERENCES appUsers(id),
    FOREIGN KEY(attendee) REFERENCES appUsers(id)
);
