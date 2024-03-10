CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL
);

CREATE TABLE training_types (
    id SERIAL PRIMARY KEY,
    training_type_name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE trainers (
    id SERIAL PRIMARY KEY,
    specialization VARCHAR(255),
    user_id INT UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE trainees (
    id SERIAL PRIMARY KEY,
    date_of_birth DATE NOT NULL,
    address TEXT,
    user_id INT UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE trainings (
    id SERIAL PRIMARY KEY,
    trainee_id INT,
    trainer_id INT,
    training_name VARCHAR(255),
    training_type_id INT,
    training_date DATE,
    training_duration INT,
    FOREIGN KEY (trainee_id) REFERENCES trainees(id) ON DELETE SET NULL,
    FOREIGN KEY (trainer_id) REFERENCES trainers(id) ON DELETE SET NULL,
    FOREIGN KEY (training_type_id) REFERENCES training_types(id) ON DELETE SET NULL
);