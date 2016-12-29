create user spring@'%' identified by 'spring123';
create database spring default charset='utf8';
grant all on spring.* to spring@'%';
use spring;
create table member(
  code     serial,
  email    varchar(255) unique,
  password varchar(2047),
  name     varchar(255)
);

create table topic(
  code   serial,
  title  varchar(2047),
  detail varchar(16383),
  photo  varchar(255),
  user   bigint
);
