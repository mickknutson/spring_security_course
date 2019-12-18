-- User Authorities
CREATE TABLE user_authorities (
    id BIGINT IDENTITY,
    appUser BIGINT NOT NULL,
    authority VARCHAR(256) NOT NULL,
);

-- user1@example.com
INSERT INTO user_authorities(appUser, authority) SELECT id,'ROLE_USER' FROM appUsers WHERE email='user1@example.com';

-- admin1@example.com
INSERT INTO user_authorities(appUser, authority) SELECT id,'ROLE_ADMIN' FROM appUsers WHERE email='admin1@example.com';
INSERT INTO user_authorities(appUser, authority) SELECT id,'ROLE_USER' FROM appUsers WHERE email='admin1@example.com';

-- user2@example.com
INSERT INTO user_authorities(appUser, authority) SELECT id,'ROLE_USER' FROM appUsers WHERE email='user2@example.com';
