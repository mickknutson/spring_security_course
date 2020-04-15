
-- since chapter04.03
-- The default appUsers_authorities data

-- user1@baselogic.com
INSERT INTO appUsers_authorities(appUsers, authority) SELECT id,'ROLE_USER' FROM appUsers WHERE email='user1@baselogic.com';

-- admin1@baselogic.com
INSERT INTO appUsers_authorities(appUsers, authority) SELECT id,'ROLE_ADMIN' FROM appUsers WHERE email='admin1@baselogic.com';
INSERT INTO appUsers_authorities(appUsers, authority) SELECT id,'ROLE_USER' FROM appUsers WHERE email='admin1@baselogic.com';

-- user2@baselogic.com
INSERT INTO appUsers_authorities(appUsers, authority) SELECT id,'ROLE_USER' FROM appUsers WHERE email='user2@baselogic.com';


-- The End...

