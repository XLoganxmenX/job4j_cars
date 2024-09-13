CREATE TABLE car
(
    id serial primary key,
    name text,
    engine_id int references engine (id) not null
);