CREATE TABLE members (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         slug VARCHAR(255),
                         name VARCHAR(255),
                         email VARCHAR(255),
                         phone_number VARCHAR(255),
                         address VARCHAR(255),
                         nik INT,
                         birthdate DATE,
                         gender VARCHAR(50),
                         status VARCHAR(50),
                         created_at BIGINT,
                         updated_at BIGINT,
                         deleted_at BIGINT
);