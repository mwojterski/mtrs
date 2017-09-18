create database mtrs;

\c mtrs -- switch to mtrs database

create user mtrs_sys
    with encrypted password 'D95iy6#48gjr$do!';

create table movie
(
    imdb_id          TEXT,
    screen_id        TEXT,
    movie_title      TEXT NOT NULL,
    available_seats  INT NOT NULL,
    reserved_seats   INT NOT NULL,
    PRIMARY KEY (imdb_id, screen_id),
    CHECK (reserved_seats <= available_seats)
);

grant select, insert, update on movie to mtrs_sys;