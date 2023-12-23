CREATE SEQUENCE customer_id_seq;

CREATE TABLE customer
(
    id    INTEGER PRIMARY KEY DEFAULT nextval('customer_id_seq'),
    name  TEXT NOT NULL,
    email TEXT NOT NULL,
--     email TEXT NOT NULL UNIQUE ,
--     password TEXT NOT NULL,
--     gender TEXT NOT NULL,
    age   INT  NOT NULL
);
