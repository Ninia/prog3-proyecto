CREATE DATABASE dwh;
USE choques;

CREATE USER test
  IDENTIFIED BY 'test';

GRANT USAGE ON *.* TO test@localhost
IDENTIFIED BY 'test';
GRANT ALL PRIVILEGES ON dwh.* TO test@localhost;