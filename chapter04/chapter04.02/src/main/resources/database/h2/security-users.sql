-- ref. Appendix A of Spring Security manual

-- chapter04.01
-- Defining users

insert into appUsers (username,password,enabled) values ('user1@example.com','user1',1);
insert into appUsers (username,password,enabled) values ('admin1@example.com','admin1',1);
insert into appUsers (username,password,enabled) values ('user2@example.com','admin1',1);
insert into appUsers (username,password,enabled) values ('disabled1@example.com','disabled1',0);
insert into appUsers (username,password,enabled) values ('admin','admin',1);

-- The End...
