create table iban
(
    id       uuid not null
      constraint iban_pk primary key,
    iban varchar(34) not null
);
grant all on table iban to postgres;

create table bankcode
(
    bankcode varchar(8) not null
        constraint bankcode_pk primary key,
    name text not null,
    short_name text not null

);
grant all on table bankcode to postgres;

