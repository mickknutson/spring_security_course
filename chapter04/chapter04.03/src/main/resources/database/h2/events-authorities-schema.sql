
-- since chapter04.03
-- The default appUsers_authorities schema

-- appUsers_authorities Schema
CREATE TABLE appUsers_authorities (
    ID BIGINT IDENTITY,
    appUsers BIGINT NOT NULL,
    authority VARCHAR(50) NOT NULL
);


-- The End...

