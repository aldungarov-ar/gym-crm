package com.spring.task.gymcrm.dao;

import com.spring.task.gymcrm.exception.DBUpdateException;
import com.spring.task.gymcrm.entity.Training;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.Duration;

@Repository
@Slf4j
@RequiredArgsConstructor
public class TrainingDAO {

    private final DataSource dataSource;

    public Training save(Training training) {
        String sql = training.getId() == null ?
                "INSERT INTO training (trainee_id, trainer_id, training_type_id, name, date, duration) VALUES (?, ?, ?, ?, ?, ?)" :
                "UPDATE training SET trainee_id = ?, trainer_id = ?, training_type_id = ?, name = ?, date = ?, duration = ? WHERE id = ?";
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, training.getTraineeId());
            statement.setLong(2, training.getTrainerId());
            statement.setLong(3, training.getTrainingTypeId());
            statement.setString(4, training.getName());
            statement.setDate(5, Date.valueOf(training.getDate()));
            statement.setLong(6, training.getDuration().toMinutes());

            if (training.getId() != null) {
                statement.setLong(7, training.getId());
            }

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                log.error("Creating training failed, no rows affected. Training: {}", training);
                throw new SQLException("Creating training failed, no rows affected.");
            }
            if (training.getId() == null) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        training.setId(generatedKeys.getLong(1));
                    } else {
                        log.error("Creating training failed, no ID obtained. Training: {}", training);
                        throw new SQLException("Creating training failed, no ID obtained.");
                    }
                }
            }
        } catch (SQLException e) {
            log.error("Error during saving training", e);
            throw new DBUpdateException("Error during saving training", e);
        }

        log.debug("Training saved to DB: {}", training);
        return training;
    }

    public Training findById(long id) throws SQLException {
        String sql = "SELECT * FROM training WHERE id = ?";
        Training training = null;
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                training = new Training();
                training.setId(resultSet.getLong("id"));
                training.setTraineeId(resultSet.getLong("trainee_id"));
                training.setTrainerId(resultSet.getLong("trainer_id"));
                training.setTrainingTypeId(resultSet.getLong("training_type_id"));
                training.setName(resultSet.getString("name"));
                training.setDate(resultSet.getDate("date").toLocalDate());
                training.setDuration(Duration.ofMinutes(resultSet.getLong("duration")));
            }
        }

        return training;
    }
}
