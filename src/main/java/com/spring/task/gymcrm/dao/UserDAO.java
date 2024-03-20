package com.spring.task.gymcrm.dao;

import com.spring.task.gymcrm.entity.User;
import com.spring.task.gymcrm.exception.DBUpdateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
@Slf4j
@RequiredArgsConstructor
public class UserDAO {
    protected final DataSource dataSource;

    public User save(User user) {
        String insertSQL = "INSERT INTO users (first_name, last_name, username, password, is_active) VALUES (?, ?, ?, ?, ?)";
        String updateSQL = user.getId() != null ?
                "UPDATE users SET first_name = ?, last_name = ?, username = ?, password = ?, is_active = ? WHERE id = ?" : insertSQL;
        log.debug("Saving user: {}. SQL: {}", user, updateSQL);

        try (PreparedStatement statement = dataSource.getConnection()
                .prepareStatement(updateSQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getUsername());
            statement.setString(4, user.getPassword());
            statement.setBoolean(5, user.isActive());

            if (user.getId() != null) {
                statement.setLong(6, user.getId());
            }

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            log.error("Failed to create new user: {}", user, e);
            throw new DBUpdateException("Failed to create new user: " + user, e);
        }
        return user;
    }

    public User findById(long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return extractUserFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            log.error("Failed to find user with ID: {}.", id, e);
            throw new DBUpdateException("Failed to find user with ID: " + id, e);
        }

        return null;
    }

    private User extractUserFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        user.setActive(resultSet.getBoolean("is_active"));
        return user;
    }

    public User update(User user) {
        if (user.getId() == null || user.getId() <= 0) {
            throw new IllegalArgumentException("User ID cannot be null or negative for update.");
        }
        return save(user);
    }

    public boolean delete(long id) {
        log.info("Deleting user with ID: {}", id);
        String sql = "DELETE FROM users WHERE id = ?";

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
            statement.setLong(1, id);
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            log.error("Failed to delete user with ID: {}.", id, e);
            throw new DBUpdateException("Failed to delete user with ID: " + id, e);
        }
    }
}
