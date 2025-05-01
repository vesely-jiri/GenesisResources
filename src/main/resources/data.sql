CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    person_id VARCHAR(255),
    uuid VARCHAR(255)
);

INSERT INTO users (first_name, last_name, person_id, uuid) VALUES ('Jan', 'Novák', '12345', 'uuid-1');
INSERT INTO users (first_name, last_name, person_id, uuid) VALUES ('Petr', 'Havel', '12346', 'uuid-2');
INSERT INTO users (first_name, last_name, person_id, uuid) VALUES ('Pavel', 'Hora', '12347', 'uuid-3');
INSERT INTO users (first_name, last_name, person_id, uuid) VALUES ('Jozef', 'Kuchař', '12348', 'uuid-4');
INSERT INTO users (first_name, last_name, person_id, uuid) VALUES ('František', 'Rota', '12349', 'uuid-5');
INSERT INTO users (first_name, last_name, person_id, uuid) VALUES ('Karel', 'Král', '12350', 'uuid-6');
INSERT INTO users (first_name, last_name, person_id, uuid) VALUES ('Martin', 'Princ', '12351', 'uuid-7');
INSERT INTO users (first_name, last_name, person_id, uuid) VALUES ('Vladimir', 'Lenin', '12352', 'uuid-8');
INSERT INTO users (first_name, last_name, person_id, uuid) VALUES ('Karel', 'Havel', '12353', 'uuid-9');
INSERT INTO users (first_name, last_name, person_id, uuid) VALUES ('Vladimir', 'Petr', '12354', 'uuid-10');