create table client
(
    id   bigserial not null primary key,
    name varchar(50) not null
);

create table address
(
    id   bigserial not null primary key,
    street varchar(255) not null,
    client_id bigint not null references client (id)
);

create table phone
(
    id   bigserial not null primary key,
    number varchar(12) not null,
    client_id bigint not null references client (id)
);