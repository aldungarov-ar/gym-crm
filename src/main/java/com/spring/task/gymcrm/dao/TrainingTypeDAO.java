package com.spring.task.gymcrm.dao;

import com.spring.task.gymcrm.entity.TrainingType;
import com.spring.task.gymcrm.exception.DBUpdateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
@Slf4j
@RequiredArgsConstructor
public class TrainingTypeDAO {
    private final DataSource dataSource;

    public TrainingType findById(Long id) {
        String sql = "SELECT * FROM training_types WHERE id = ?";
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                TrainingType trainingType = new TrainingType();
                trainingType.setId(resultSet.getLong("id"));
                trainingType.setName(resultSet.getString("name"));
                return trainingType;
            }
        } catch (SQLException e) {
            log.error("Failed to find training type with ID: {}.", id, e);
            throw new DBUpdateException("Failed to find training type with ID: " + id, e);
        }

        return null;
    }
}
