
-- chapter04.02

-----
-- Create the Groups
INSERT INTO groups(group_name) VALUES ('Users');
INSERT INTO groups(group_name) VALUES ('Administrators');


-----
-- Map the Groups to Roles
INSERT INTO group_authorities(group_id, authority) SELECT id,'ROLE_USER' FROM groups WHERE group_name='Users';


-- Administrators are both a ROLE_USER and ROLE_ADMIN
INSERT INTO group_authorities(group_id, authority) SELECT id,'ROLE_USER' FROM groups WHERE group_name='Administrators';
INSERT INTO group_authorities(group_id, authority) SELECT id,'ROLE_ADMIN' FROM groups WHERE group_name='Administrators';


-----
-- Map the users to Groups
INSERT INTO group_members(group_id, username) SELECT id,'user1@baselogic.com' FROM groups WHERE group_name='Users';
INSERT INTO group_members(group_id, username) SELECT id,'admin1@baselogic.com' FROM groups WHERE group_name='Administrators';
INSERT INTO group_members(group_id, username) SELECT id,'user2@baselogic.com' FROM groups WHERE group_name='Users';
INSERT INTO group_members(group_id, username) SELECT id,'disabled1@baselogic.com' FROM groups WHERE group_name='Users';


-- The End...
