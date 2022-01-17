create table example_table
(
    id           character varying(64) not null primary key,
    name         character varying(100),
    created_date date                  not null default now(),
    created_time timestamp             not null,
    is_active    boolean                        default false,
    counter      int                   not null default 0,
    currency     decimal               not null,
    description  text,
    floating     double precision
);
