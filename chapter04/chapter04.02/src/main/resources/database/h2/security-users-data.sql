-- ref. Appendix A of Spring Security manual

-- chapter04.01
-- Defining Security users

insert into users (username,password,enabled) values ('user1@baselogic.com','{noop}user1',1);
insert into users (username,password,enabled) values ('admin1@baselogic.com','{noop}admin1',1);
insert into users (username,password,enabled) values ('user2@baselogic.com','{noop}admin1',1);

-- Disabled User
insert into users (username,password,enabled) values ('disabled1@baselogic.com','{noop}disabled1',0);


-- Chuck (for testing)
insert into users (username,password,enabled) values ('chuck@baselogic.com','{noop}chucknorris',1);

-- The End...
