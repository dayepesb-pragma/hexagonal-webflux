CREATE TABLE IF NOT EXISTS skillnettest."user" (
    id bigserial NOT NULL,
    name varchar(255) NULL,
    email varchar(255) NULL,
    phone_number varchar(255) NULL,
    address varchar(255) NULL,
    identification varchar NULL,
    identification_type varchar NULL,
    CONSTRAINT user_pkey PRIMARY KEY (id)
);

CREATE SEQUENCE IF NOT EXISTS skillnettest.user_id_seq;
ALTER TABLE skillnettest."user" ALTER COLUMN id SET DEFAULT nextval('skillnettest.user_id_seq');