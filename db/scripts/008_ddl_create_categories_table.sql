create table categories(
    id serial primary key,
    name varchar(25) unique not null
)