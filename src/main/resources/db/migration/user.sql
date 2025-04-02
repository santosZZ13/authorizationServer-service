CREATE TABLE IF NOT EXISTS app_user
(
    id          uuid                        NOT NULL PRIMARY KEY,
    email    varchar(64)                 NOT NULL,
    password    varchar(64)   DEFAULT NULL,
    first_name  varchar(32)   DEFAULT NULL,
--     middle_name varchar(32)   DEFAULT NULL,
    last_name   varchar(32)   DEFAULT NULL,
    locale      varchar(2)    DEFAULT NULL,
    avatar_url  varchar(2048) DEFAULT NULL,
    email_verified boolean      DEFAULT false NOT NULL,
    active      boolean       DEFAULT false NOT NULL,
    provider    varchar(32)   DEFAULT NULL,
    created_at  timestamp without time zone NOT NULL,
    updated_at  timestamp without time zone NOT NULL
    );

CREATE TABLE IF NOT EXISTS role
(
    id   serial      NOT NULL PRIMARY KEY,
    name varchar(64) NOT NULL
    );

CREATE TABLE IF NOT EXISTS user_role
(
    user_id uuid    NOT NULL,
    role_id integer NOT NULL
);

CREATE TABLE IF NOT EXISTS authority
(
    id   serial      NOT NULL PRIMARY KEY,
    name varchar(32) NOT NULL
    );

CREATE TABLE IF NOT EXISTS role_authority
(
    role_id      integer NOT NULL,
    authority_id integer NOT NULL
);


INSERT INTO app_user (id, email, password, first_name, last_name, locale, avatar_url, email_verified, active, provider, created_at, updated_at)
VALUES ('7f000001-8a56-11d1-818a-56e25ae30000', 'admin@localhost', 'admin', 'quang', 'nam', 'en', NULL, true, true, 'local', now(), now());


INSERT INTO role (id, name) VALUES (1, 'ADMIN');
INSERT INTO role (id, name) VALUES (2, 'USER');

INSERT INTO user_role (user_id, role_id) VALUES ('7f000001-8a56-11d1-818a-56e25ae30000', 1);
INSERT INTO user_role (user_id, role_id) VALUES ('7f000001-8a56-1695-818a-56687e770000', 2);

INSERT INTO authority (id, name) VALUES (1, 'ARTICLE_READ');
INSERT INTO authority (id, name) VALUES (2, 'ARTICLE_WRITE');

-- ADMIN can read and write
INSERT INTO role_authority (role_id, authority_id) VALUES (1, 1);
INSERT INTO role_authority (role_id, authority_id) VALUES (1, 2);

-- USER only can read
INSERT INTO role_authority (role_id, authority_id) VALUES (2, 1);