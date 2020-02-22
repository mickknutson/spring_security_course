
-- since chapter04.03
-- The default appUsers_authorities data

-- user1@example.com
INSERT INTO appUsers_authorities(appUsers, authority) SELECT id,'ROLE_USER' FROM appUsers WHERE email='user1@example.com';

-- admin1@example.com
INSERT INTO appUsers_authorities(appUsers, authority) SELECT id,'ROLE_ADMIN' FROM appUsers WHERE email='admin1@example.com';
INSERT INTO appUsers_authorities(appUsers, authority) SELECT id,'ROLE_USER' FROM appUsers WHERE email='admin1@example.com';

-- user2@example.com
INSERT INTO appUsers_authorities(appUsers, authority) SELECT id,'ROLE_USER' FROM appUsers WHERE email='user2@example.com';


-- The End...

