CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    person_id VARCHAR(255),
    uuid VARCHAR(255)
);

INSERT INTO users (first_name, last_name, person_id, uuid) VALUES
    ('Jan', 'Novák', '12345', 'b370a105-70ed-4b48-8161-7a577ac5710b'),
    ('Petr', 'Havel', '12346', '1270cd80-5244-419b-806c-1cec5753b3b2'),
    ('Pavel', 'Hora', '12347', 'cbac23a0-87f5-48c8-b174-6d62eeefbd65'),
    ('Jozef', 'Kuchař', '12348', 'c6bc78c4-c184-44b2-87f3-37ffe3b46f90'),
    ('František', 'Rota', '12349', 'af2060da-7ff8-4a18-a6b8-d326f6930235'),
    ('Karel', 'Král', '12350', '492f0e0a-746d-4932-a4c3-a7bd5a5bd2ff'),
    ('Martin', 'Princ', '12351', '849ae50d-f6ef-459b-ac88-d71ec0abfd15'),
    ('Vladimir', 'Lenin', '12352', '7bd5c5ff-a490-411c-90df-8ea19ac63650'),
    ('Karel', 'Havel', '12353', '48e286f7-4d64-4f25-8605-f44325db4931'),
    ('Vladimir', 'Petr', '12354', 'fed86cda-60aa-4113-b95b-cfe90993c7ce');