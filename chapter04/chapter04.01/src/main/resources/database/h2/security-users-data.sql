-- ref. Appendix A of Spring Security manual

-- chapter04.01
-- Defining Security users

insert into users (username,password,enabled) values ('user1@baselogic.com','{noop}user1',1);
insert into users (username,password,enabled) values ('admin1@baselogic.com','{noop}admin1',1);
insert into users (username,password,enabled) values ('user2@baselogic.com','{noop}admin1',1);
insert into users (username,password,enabled) values ('admin','{noop}admin',1);

insert into users (username,password,enabled) values ('disabled1@baselogic.com','{noop}disabled1',0);

-- The End...
