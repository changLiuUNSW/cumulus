# --- First database schema

# --- !Ups

CREATE TABLE account (
  id       UUID          PRIMARY KEY,
  mail     VARCHAR(255)  NOT NULL,
  login    VARCHAR(64)   NOT NULL,
  password VARCHAR(64)   NOT NULL,
  creation TIMESTAMP     NOT NULL,
  roles    VARCHAR(64)[] NOT NULL,
  home     TEXT
);

CREATE UNIQUE INDEX account_mail_unique ON account (LOWER(mail));
CREATE UNIQUE INDEX account_login_unique ON account (LOWER(login));

# --- !Downs

DROP TABLE IF EXISTS account;