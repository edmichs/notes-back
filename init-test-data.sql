INSERT INTO roles (id, name, created_at, updated_at)
VALUES
    (1, "admin", NOW(), NOW()),
    (2, "user", NOW(), NOW());

INSERT INTO users (id, username, email, password, created_at, updated_at)
VALUES (1, 'testuser', 'test@example.com', '$2a$10$TeGQYjxkK7QUz.IpY5NJ/O1G7r2Fbtn1KlULIp3kPULRO9rVlvd9m', NOW(), NOW());

INSERT INTO user_roles (user_id, role_id)
VALUES (1, 2);


INSERT INTO notes (id, title, content, owner_id, created_at, updated_at)
VALUES
    (1, 'Test Note 1', 'This is the first test note content.', 1, NOW(), NOW()),
    (2, 'Test Note 2', 'This is the second test note content.', 1, NOW(), NOW()),
    (3, 'Another Note', 'This is another test note with different content.', 1, NOW(), NOW());

-- Reset sequences
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
SELECT setval('notes_id_seq', (SELECT MAX(id) FROM notes));