-- ref. Appendix A of Spring Security manual

-- chapter04.01
-- Defining user authorities

INSERT INTO authorities(username,authority) VALUES ('user1@baselogic.com','ROLE_USER');

INSERT INTO authorities(username,authority) VALUES ('admin1@baselogic.com','ROLE_ADMIN');
INSERT INTO authorities(username,authority) VALUES ('admin1@baselogic.com','ROLE_USER');

INSERT INTO authorities(username,authority) VALUES ('user2@baselogic.com','ROLE_USER');

-- User is disabled
INSERT INTO authorities(username,authority) VALUES ('disabled1@baselogic.com','ROLE_USER');


-- Chuck (for testing)
INSERT INTO authorities(username,authority) VALUES ('chuck@baselogic.com','ROLE_USER');

-- The End...
