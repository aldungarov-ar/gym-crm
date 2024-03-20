CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    username TEXT NOT NULL,
    password TEXT NOT NULL,
    is_active BOOLEAN NOT NULL
);


CREATE TABLE trainees (
    id BIGSERIAL PRIMARY KEY,
    date_of_birth DATE,
    address TEXT,
    user_id INTEGER UNIQUE REFERENCES users(id) ON DELETE CASCADE
);


CREATE TABLE trainers (
    id BIGSERIAL PRIMARY KEY,
    specialization_id INTEGER,
    user_id INTEGER UNIQUE REFERENCES users(id) ON DELETE CASCADE
);


CREATE TABLE training_types (
    id BIGSERIAL PRIMARY KEY,
    training_type_name TEXT NOT NULL
);


CREATE TABLE trainer_trainee (
    trainee_id INTEGER REFERENCES trainees(id) ON DELETE CASCADE,
    trainer_id INTEGER REFERENCES trainers(id) ON DELETE CASCADE,
    PRIMARY KEY (trainee_id, trainer_id)
);


CREATE TABLE training (
    id SERIAL PRIMARY KEY,
    trainee_id INTEGER REFERENCES trainees(id) ON DELETE CASCADE,
    trainer_id INTEGER REFERENCES trainers(id),
    training_name TEXT NOT NULL,
    training_type_id INTEGER REFERENCES training_types(id),
    training_date DATE NOT NULL,
    training_duration INTEGER NOT NULL
);
