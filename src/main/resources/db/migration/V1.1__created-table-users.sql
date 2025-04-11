create table users (
    id INT NOT NULL AUTO_INCREMENT,
    plan_id INT NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    username VARCHAR(50) UNIQUE,
    birth_date DATETIME,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(10) UNIQUE,
    address VARCHAR(255),
    PRIMARY KEY (id)

);
