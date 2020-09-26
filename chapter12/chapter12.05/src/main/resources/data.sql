-- In Spring Boot using Spring Data, we use ~/src/main/resources/data.sql to seed data.


-- appUsers Data
-- Password for user1 was 'user1'
INSERT INTO app_users (id,email,password,first_name,last_name) VALUES (0,'user1@baselogic.com','{bcrypt}$2a$04$gBdMIzQ5P2Ffb4L/epcKSOiYRlwPcUKx1jlfENvOUMpSAm4PsRdK2','User','One');
INSERT INTO app_users (id,email,password,first_name,last_name) VALUES (1,'admin1@baselogic.com','{bcrypt}$2a$04$bGt.Kbtc8OaqzzjFqAzLwu5tc90IpYD5P5hSB61ZmjpIyo4.nlub6','Admin','One');
INSERT INTO app_users (id,email,password,first_name,last_name) VALUES (2,'user2@baselogic.com','{bcrypt}$2a$04$erLmf9XQ3hPLDBb0eTdpReVVwESHmeMUrCnR722EEgVUfC95NU3Ra','User','Two');

-- ROLES --
INSERT INTO roles (id, name) values (0, 'ROLE_USER');
INSERT INTO roles (id, name) values (1, 'ROLE_ADMIN');


-- user1 has one role
INSERT INTO user_role (user_id,role_id) values (0, 0);

-- admin1 has two roles
INSERT INTO user_role (user_id,role_id) values (1, 0);
INSERT INTO user_role (user_id,role_id) values (1, 1);


-- Event Data
INSERT INTO events (id,event_date,summary,description,owner,attendee) VALUES (100,'2020-07-03 00:00:01','Birthday Party','Time to have my yearly party!',0,1);
INSERT INTO events (id,event_date,summary,description,owner,attendee) VALUES (101,'2020-12-23 13:00:00','Mountain Bike Race','Deer Valley mountain bike race',2,0);
INSERT INTO events (id,event_date,summary,description,owner,attendee) VALUES (102,'2020-01-23 11:30:00','Lunch','Eating lunch together',1,2);


-- The End...
