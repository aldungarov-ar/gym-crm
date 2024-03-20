package com.spring.task.gymcrm.dao;

import com.spring.task.gymcrm.entity.Trainee;
import com.spring.task.gymcrm.entity.User;
import com.spring.task.gymcrm.exception.DBUpdateException;
import com.spring.task.gymcrm.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
@Slf4j
public class TraineeDAO extends UserDAO {

    public TraineeDAO(DataSource dataSource) {
        super(dataSource);
    }

    public Trainee save(Trainee trainee) {
        User savedUser = super.save(trainee);
        trainee.setId(savedUser.getId());

        String sql;
        if (traineeExists(trainee.getId())) {
            sql = "UPDATE trainees SET date_of_birth = ?, address = ? WHERE user_id = ?";
        } else {
            sql = "INSERT INTO trainees (date_of_birth, address, user_id) VALUES (?, ?, ?)";
        }

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
            statement.setDate(1, Date.valueOf(trainee.getDateOfBirth()));
            statement.setString(2, trainee.getAddress());
            statement.setLong(3, trainee.getId());

            log.debug("Saving trainee: {}. SQL: {}", trainee, statement);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Failed to save trainee: {}. SQL: {}", trainee, sql, e);
            throw new DBUpdateException("Failed to save trainee: " + trainee + ". SQL: " + sql, e);
        }
        log.debug("Trainee saved to DB: {}", trainee);
        return trainee;
    }

    @Override
    public Trainee findById(long id) {
        User user = super.findById(id);
        if (user == null) {
            return null;
        }

        String sql = "SELECT * FROM trainees WHERE user_id = ?";
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Trainee trainee = new Trainee(user);
                trainee.setDateOfBirth(resultSet.getDate("date_of_birth").toLocalDate());
                trainee.setAddress(resultSet.getString("address"));
                return trainee;
            }
        } catch (SQLException e) {
            log.error("Failed to find trainee with ID: {}.", id, e);
            throw new EntityNotFoundException("Failed to find trainee with ID: " + id, e);
        }

        return null;
    }

    private boolean traineeExists(long userId) {
        String sql = "SELECT 1 FROM trainees WHERE user_id = ?";
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            log.error("Failed to check if trainee exists with user ID: {}. Request: {}", userId, sql, e);
            throw new EntityNotFoundException("Failed to check if trainee exists with user ID: " + userId + ". Request: " + sql, e);
        }
    }

    @Override
    public boolean delete(long userId) {
        String sql = "DELETE FROM trainees WHERE user_id = ?";
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
            statement.setLong(1, userId);
            log.info("Deleting trainee with user ID: {}.", userId);
            return statement.executeUpdate() > 0 && super.delete(userId);
        } catch (SQLException e) {
            log.error("Failed to delete trainee with user ID: {}. SQL: {}", userId, sql, e);
            throw new DBUpdateException("Failed to delete trainee with user ID: " + userId + ". SQL: " + sql, e);
        }
    }
}
