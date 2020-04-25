-- ref. Appendix A of Spring Security manual

-- chapter04.01
-- The default USERS schema of Spring Security

CREATE TABLE users(
    username varchar(256) not null primary key,
    password varchar(256) not null,
    enabled boolean not null
);


CREATE TABLE authorities (
    username varchar(256) not null,
    authority varchar(256) not null,
    constraint fk_authorities_users foreign key(username) references users(username)
);


CREATE UNIQUE INDEX ix_auth_username on authorities (username,authority);

-- The End...
