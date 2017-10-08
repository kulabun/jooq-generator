CREATE TABLE person (
  id              BIGINT PRIMARY KEY,
  first_name      VARCHAR(256),
  last_name       VARCHAR(256),
  age             INTEGER,
  birth_date      DATE,
  created         DATETIME,
  last_modified   TIMESTAMP,
  profile_picture LONGBLOB
)