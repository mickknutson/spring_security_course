-- User Authorities
CREATE TABLE user_authorities (
    id BIGINT IDENTITY,
    user BIGINT NOT NULL,
    authority VARCHAR(256) NOT NULL,
);

-- user1@example.com
INSERT INTO user_authorities(user, authority) SELECT id,'ROLE_USER' FROM users WHERE email='user1@example.com';

-- admin1@example.com
INSERT INTO user_authorities(user, authority) SELECT id,'ROLE_ADMIN' FROM users WHERE email='admin1@example.com';
INSERT INTO user_authorities(user, authority) SELECT id,'ROLE_USER' FROM users WHERE email='admin1@example.com';

-- user2@example.com
INSERT INTO user_authorities(user, authority) SELECT id,'ROLE_USER' FROM users WHERE email='user2@example.com';
