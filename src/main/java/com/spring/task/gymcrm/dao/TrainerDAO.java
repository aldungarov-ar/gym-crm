package com.spring.task.gymcrm.dao;

import com.spring.task.gymcrm.entity.Trainer;
import com.spring.task.gymcrm.entity.User;
import com.spring.task.gymcrm.exception.DBUpdateException;
import com.spring.task.gymcrm.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
@Slf4j
public class TrainerDAO extends UserDAO {
    public TrainerDAO(DataSource dataSource) {
        super(dataSource);
    }

    public Trainer save(Trainer trainer) {
        User savedUser = super.save(trainer);
        trainer.setId(savedUser.getId());

        String sql;
        if (trainerExists(trainer.getId())) {
            sql = "UPDATE trainers SET specialization_id = ? WHERE user_id = ?";
        } else {
            sql = "INSERT INTO trainers (specialization_id, user_id) VALUES (?, ?)";
        }

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
            statement.setString(1, String.valueOf(trainer.getSpecializationId()));
            statement.setLong(2, trainer.getId());

            log.debug("Saving trainer: {}. SQL: {}", trainer, statement);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Failed to save trainer: {}. SQL: {}", trainer, sql, e);
            throw new DBUpdateException("Failed to save trainer: " + trainer + ". SQL: " + sql, e);
        }
        log.debug("Trainer saved to DB: {}", trainer);
        return trainer;
    }

    public Trainer findById(long id) {
        User user = super.findById(id);
        if (user == null) {
            return null;
        }

        String sql = "SELECT * FROM trainers WHERE user_id = ?";
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Trainer trainer = new Trainer(user);
                trainer.setSpecializationId(resultSet.getLong("specialization_id"));
                return trainer;
            }
        } catch (SQLException e) {
            log.error("Failed to find trainer by id: {}. SQL: {}", id, sql, e);
            throw new EntityNotFoundException("Failed to find trainer by id: " + id + ". SQL: " + sql, e);
        }

        return null;
    }

    private boolean trainerExists(Long id) {
        String sql = "SELECT 1 FROM trainers WHERE user_id = ?";
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
            statement.setLong(1, id);
            return statement.executeQuery().next();
        } catch (SQLException e) {
            log.error("Failed to check if trainer exists. SQL: {}", sql, e);
            throw new EntityNotFoundException("Failed to check if trainer exists. SQL: " + sql, e);
        }
    }
}
