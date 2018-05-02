CREATE TABLE IF NOT EXISTS vets (
  id              INT(4) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  first_name      VARCHAR(30),
  last_name       VARCHAR(30),
  age             INTEGER,
  birth_date      DATE,
  created         DATETIME,
  last_modified   TIMESTAMP,
  profile_picture LONGBLOB
);
CREATE INDEX ON vets (last_name);

CREATE TABLE IF NOT EXISTS specialties (
  id   INT(4) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(80)
);
CREATE INDEX ON specialties (name);

CREATE TABLE IF NOT EXISTS vet_specialties (
  vet_id       INT(4) NOT NULL,
  specialty_id INT(4) NOT NULL,
  FOREIGN KEY (vet_id) REFERENCES vets (id),
  FOREIGN KEY (specialty_id) REFERENCES specialties (id),
  UNIQUE (vet_id, specialty_id)
);

CREATE TABLE IF NOT EXISTS types (
  id   INT(4) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(80),
);
CREATE INDEX ON types(name);

CREATE TABLE IF NOT EXISTS owners (
  id         INT(4) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(30),
  last_name  VARCHAR(30),
  address    VARCHAR(255),
  city       VARCHAR(80),
  telephone  VARCHAR(20)
);
CREATE INDEX ON owners (last_name);

CREATE TABLE IF NOT EXISTS pets (
  id         INT(4) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name       VARCHAR(30),
  birth_date DATE,
  type_id    INT(4) NOT NULL,
  owner_id   INT(4) NOT NULL,
  FOREIGN KEY (owner_id) REFERENCES owners (id),
  FOREIGN KEY (type_id) REFERENCES types (id)
);
CREATE INDEX ON pets (name);

CREATE TABLE IF NOT EXISTS visits (
  id          INT(4) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  pet_id      INT(4) NOT NULL,
  visit_date  DATE,
  description VARCHAR(255),
  FOREIGN KEY (pet_id) REFERENCES pets (id)
);
