create database weather;

create table users
(
    id       serial primary key,
    login    varchar(128) not null unique,
    password varchar(128) not null
);

create table sessions
(
    id        uuid      not null primary key,
    userid    integer   not null references users,
    expiresat timestamp not null
);

create table locations
(
    id        serial primary key,
    name      varchar(128) not null,
    state     varchar(128),
    userid    integer      not null references users,
    latitude  numeric      not null,
    longitude numeric      not null,
    unique (userid, latitude, longitude)
);