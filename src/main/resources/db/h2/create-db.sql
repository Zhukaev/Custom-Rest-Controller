CREATE TABLE IF NOT EXISTS users
 (id INT NOT NULL AUTO_INCREMENT,
 phone VARCHAR NOT NULL UNIQUE,
 firstname VARCHAR NOT NULL,
 lastname VARCHAR NOT NULL,
 PRIMARY KEY (id));