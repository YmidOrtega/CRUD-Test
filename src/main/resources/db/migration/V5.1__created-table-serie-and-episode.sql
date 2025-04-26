CREATE TABLE series (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    description TEXT,
    genre VARCHAR(100),
    language VARCHAR(100),
    release_date DATE,
    end_date DATE,
    image_url VARCHAR(255),
    active BOOLEAN
);

CREATE TABLE episodes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    serie_id BIGINT NOT NULL,
    name VARCHAR(255),
    description TEXT,
    launch_date DATE,
    FOREIGN KEY (serie_id) REFERENCES series(id) ON DELETE CASCADE
);
