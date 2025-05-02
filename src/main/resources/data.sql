CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    person_id VARCHAR(255) UNIQUE,
    uuid VARCHAR(255) UNIQUE
);

--- Test data
INSERT INTO users (first_name, last_name, person_id, uuid) VALUES
    ('Jan', 'Novák', 'jXa4g3H7oPq2', 'b370a105-70ed-4b48-8161-7a577ac5710b'),
    ('Petr', 'Havel', 'yB9fR6tK0wLm', '1270cd80-5244-419b-806c-1cec5753b3b2'),
    ('Pavel', 'Hora', 'cN1vZ8pE5sYx', 'cbac23a0-87f5-48c8-b174-6d62eeefbd65'),
    ('Jozef', 'Kuchař', 'tQdG2kP3mJfB', 'c6bc78c4-c184-44b2-87f3-37ffe3b46f90'),
    ('František', 'Rota', 'iM5sO6zXcW7v', 'af2060da-7ff8-4a18-a6b8-d326f6930235'),
    ('Karel', 'Král', 'rU8nA9eT2bYh', '492f0e0a-746d-4932-a4c3-a7bd5a5bd2ff'),
    ('Martin', 'Princ', 'wV6eH1fK7qZj', '849ae50d-f6ef-459b-ac88-d71ec0abfd15'),
    ('Vladimir', 'Lenin', 'sL4gN9dC3bXz', '7bd5c5ff-a490-411c-90df-8ea19ac63650'),
    ('Karel', 'Havel', 'kR0aZ7vW2nDl', '48e286f7-4d64-4f25-8605-f44325db4931'),
    ('Vladimir', 'Petr', 'eI1oY6tQ9dKj', 'fed86cda-60aa-4113-b95b-cfe90993c7ce');