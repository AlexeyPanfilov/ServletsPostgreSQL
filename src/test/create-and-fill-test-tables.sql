BEGIN;

DROP TABLE IF EXISTS persons CASCADE;

DROP TABLE IF EXISTS banks CASCADE;

DROP TABLE IF EXISTS cards CASCADE;

DROP TABLE IF EXISTS banks_persons CASCADE;

CREATE TABLE banks (id BIGSERIAL primary key, title VARCHAR(50));

INSERT INTO banks (title)
VALUES ('Sber'),
       ('VTB'),
       ('Alfa'),
       ('GPB');

CREATE TABLE persons (id BIGSERIAL primary key, name VARCHAR(50), lastname VARCHAR(50));

INSERT INTO persons (name, lastname)
VALUES ('John', 'Smith'),
       ('Vladimir', 'Ivanov'),
       ('Petr', 'Petrov'),
       ('Mika', 'Hakkinen');

CREATE TABLE cards (
    id BIGSERIAL primary key,
    number VARCHAR(9),
    bank_id BIGINT REFERENCES banks (id) ON DELETE CASCADE,
    person_id BIGINT REFERENCES persons (id) ON DELETE CASCADE
                   );

INSERT INTO cards (number, bank_id, person_id)
VALUES ('1111 5678', 1, 1),
       ('1111 1234', 1, 2),
       ('1111 2367', 1, 3),
       ('2222 5555', 2, 4),
       ('2222 1234', 2, 1),
       ('3333 1234', 3, 2),
       ('3333 9876', 3, 3),
       ('4444 5555', 4, 1),
       ('4444 1212', 4, 1),
       ('4444 5633', 4, 4);

CREATE TABLE banks_persons (
    bank_id BIGINT,
    person_id BIGINT,
    foreign key (bank_id) REFERENCES banks(id) ON DELETE CASCADE,
    foreign key (person_id) REFERENCES persons(id) ON DELETE CASCADE
                           );

INSERT INTO banks_persons (bank_id, person_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (2, 4),
       (2, 1),
       (3, 2),
       (3, 3),
       (4, 1),
       (4, 1),
       (4, 4);

COMMIT;