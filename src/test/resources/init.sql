CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL
);

CREATE TABLE training_types (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE trainers (
    user_id INT UNIQUE,
    specialization_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (specialization_id) REFERENCES training_types(id) ON DELETE SET NULL
);

CREATE TABLE trainees (
    user_id BIGSERIAL PRIMARY KEY,
    address TEXT,
    date_of_birth DATE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE trainings (
    id BIGSERIAL PRIMARY KEY,
    trainee_id INT,
    trainer_id INT,
    training_name VARCHAR(255),
    training_type_id INT,
    training_date DATE,
    training_duration INT,
    FOREIGN KEY (trainee_id) REFERENCES trainees(user_id) ON DELETE SET NULL,
    FOREIGN KEY (trainer_id) REFERENCES trainers(user_id) ON DELETE SET NULL,
    FOREIGN KEY (training_type_id) REFERENCES training_types(id) ON DELETE SET NULL
);