-- Créer la base de données
-- CREATE DATABASE under_my_hat;

-- Se connecter à la base de données
-- \c under_my_hat;

-- Créer la table `user`
CREATE TABLE IF NOT EXISTS users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(100) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);