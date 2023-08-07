-- Version: V1__Create_client_table.sql

CREATE TABLE client (
    id TEXT PRIMARY KEY UNIQUE NOT NULL,
    email VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    client_role ENUM('ROLE_ADMIN', 'ROLE_USER') NOT NULL
);
