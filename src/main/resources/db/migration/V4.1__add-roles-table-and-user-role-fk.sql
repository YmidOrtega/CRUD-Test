CREATE TABLE roles (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

INSERT INTO roles (id, name) VALUES
(1, 'ROLE_USER'),
(2, 'ROLE_ADMIN'),
(3, 'ROLE_SUPER_ADMIN');


ALTER TABLE users ADD COLUMN role_id BIGINT;

UPDATE users SET role_id = 1;

ALTER TABLE users MODIFY COLUMN role_id BIGINT NOT NULL;
ALTER TABLE users ADD CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles(id);