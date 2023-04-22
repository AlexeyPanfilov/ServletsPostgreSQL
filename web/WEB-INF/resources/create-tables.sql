BEGIN;

CREATE TABLE IF NOT EXISTS banks (id BIGSERIAL primary key, title VARCHAR(50));

CREATE TABLE IF NOT EXISTS persons (id BIGSERIAL primary key, name VARCHAR(50), lastname VARCHAR(50));

CREATE TABLE IF NOT EXISTS cards (
                       id BIGSERIAL primary key,
                       number VARCHAR(9),
                       bank_id BIGINT REFERENCES banks (id) ON DELETE CASCADE,
                       person_id BIGINT REFERENCES persons (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS banks_persons (
                               bank_id BIGINT,
                               person_id BIGINT,
                               foreign key (bank_id) REFERENCES banks(id) ON DELETE CASCADE,
                               foreign key (person_id) REFERENCES persons(id) ON DELETE CASCADE
);

COMMIT;