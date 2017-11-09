DROP DATABASE IF EXISTS users;
CREATE DATABASE users DEFAULT CHARACTER SET utf8;

USE users;

CREATE TABLE user_log (
  id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
  user_name varchar(100) NOT NULL,
  time_stamp varchar(100) NOT NULL,
  info varchar(100) NOT NULL
);


USE users;

CREATE TABLE user_sign_up (
  id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
  user_name varchar(100) NOT NULL,
  first_name varchar(100) NOT NULL,
  last_name varchar(100) NOT NULL,
  email varchar(100) NOT NULL,
  pass varchar(100) NOT NULL,
  jira_user varchar(100),
  jira_pass varchar(100)
  );



USE users;

CREATE TABLE retro_detail (
  id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
  user_name varchar(100) NOT NULL,
  time_stamp varchar(100) NOT NULL,
  retro_message varchar(100) NOT NULL,
  is_active boolean NOT NULL
  );
