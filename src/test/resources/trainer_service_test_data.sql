INSERT INTO users (id, first_name, last_name, username, password, is_active) VALUES (100, 'Sam', 'Gamgee', 'Sam.Gamgee', 'SomeValidPassword', true);
INSERT INTO users (id, first_name, last_name, username, password, is_active) VALUES (101, 'Frodo', 'Baggins', 'Frodo.Baggins', 'SomeValidPassword', true);
INSERT INTO users (id, first_name, last_name, username, password, is_active) VALUES (102, 'Gandalf', 'TheGrey', 'Gandalf.TheGrey', 'YouShellNotPass', true);
INSERT INTO users (id, first_name, last_name, username, password, is_active) VALUES (103, 'Aragorn', 'SonOfArathorn', 'Aragorn.SonOfArathorn', 'SomeValidPassword', true);
INSERT INTO users (id, first_name, last_name, username, password, is_active) VALUES (104, 'Legolas', 'Greenleaf', 'Legolas.Greenleaf', 'SomeValidPassword', true);

INSERT INTO training_types (id, training_type_name) VALUES (10, 'Walking to Mordor');
INSERT INTO training_types (id, training_type_name) VALUES (11, 'Fighting');

INSERT INTO trainees (id, date_of_birth, address, user_id) VALUES (1000, '1990-01-01', 'Shire', 100);
INSERT INTO trainees (id, date_of_birth, address, user_id) VALUES (1001, '1990-01-01', 'Shire', 101);

INSERT INTO trainers (id, specialization_id, user_id) VALUES (2000, 10, 102);
INSERT INTO trainers (id, specialization_id, user_id) VALUES (2001, 11, 103);
INSERT INTO trainers (id, specialization_id, user_id) VALUES (2002, 10, 104);

INSERT INTO trainer_trainee (trainee_id, trainer_id) VALUES (1000, 2000);
INSERT INTO trainer_trainee (trainee_id, trainer_id) VALUES (1000, 2001);
INSERT INTO trainer_trainee (trainee_id, trainer_id) VALUES (1001, 2001);