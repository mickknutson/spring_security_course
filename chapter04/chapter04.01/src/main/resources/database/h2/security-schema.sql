-- ref. Appendix A of Spring Security manual

CREATE TABLE appUsers(
    username varchar(256) not null primary key,
    password varchar(256) not null,
    enabled boolean not null
);

CREATE TABLE authorities (
    username varchar(256) not null,
    authority varchar(256) not null,
    constraint fk_authorities_users foreign key(username) references appUsers(username)
);
CREATE UNIQUE INDEX ix_auth_username on authorities (username,authority);
