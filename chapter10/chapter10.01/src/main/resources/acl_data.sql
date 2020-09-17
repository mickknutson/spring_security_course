
--- ACLs ----------------------------------------
-- Event Class to protect:
insert into acl_class (id, class) values (10, 'io.baselogic.springsecurity.domain.Event');

-- SIDs
-- User specific:
insert into acl_sid (id, principal, sid) values (20, true, 'user2@example.com');

-- Role specific:
insert into acl_sid (id, principal, sid) values (21, false, 'ROLE_USER');
insert into acl_sid (id, principal, sid) values (22, false, 'ROLE_ADMIN');


-- object identity
-- Event entry for user2 SID
insert into acl_object_identity (id,object_id_identity,object_id_class,parent_object,owner_sid,entries_inheriting)
values (30,100, 10, null, 20, false);

-- Event entry for ROLE_USER SID
insert into acl_object_identity (id,object_id_identity,object_id_class,parent_object,owner_sid,entries_inheriting)
values (31,101, 10, null, 21, false);

-- Event entry for ROLE_ADMIN SID
insert into acl_object_identity (id,object_id_identity,object_id_class,parent_object,owner_sid,entries_inheriting)
values (32,102, 10, null, 21, false);


-- ACEntry list ---------------------------------
-- mask == R
-- Entry for Event entry for user2 SID
insert into acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
values(30, 1, 20, 1, true, true, true);

-- the end --
