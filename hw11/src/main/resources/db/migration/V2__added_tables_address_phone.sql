alter table client add column address_id bigint;
create table address
(
    id   bigserial not null primary key,
    street varchar(255)
);

create table phone
(
    id   bigserial not null primary key,
    number varchar(12),
    client_id bigint
);