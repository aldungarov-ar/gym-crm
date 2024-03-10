package com.spring.task.gymcrm.dao;

import com.spring.task.gymcrm.entity.User;
import com.spring.task.gymcrm.exception.DBUpdateException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;

@Repository
public class UserDAO extends BaseDAO {
    private final RowMapper<User> rowMapper = (ResultSet rs, int rowNum) -> {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setUsername(rs.getString("username"));
        user.setIsActive(rs.getBoolean("is_active"));

        return user;
    };

    public long save(User user) throws DBUpdateException {
        String sql = "INSERT INTO users (first_name, last_name, username, password, is_active) VALUES (?, ?, ?, ?, ?)";
        return update(sql, user.getFirstName(), user.getLastName(), user.getUsername(), user.getPassword(), user.getIsActive());
    }

    public void update(User user) throws DBUpdateException {
        String sql = "UPDATE users SET first_name = ?, last_name = ?, username = ?, password = ?, is_active = ?";
        update(sql, user.getFirstName(), user.getLastName(), user.getUsername(), user.getPassword(), user.getIsActive());
    }

    public User findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return find(sql, rowMapper, id);
    }

    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        return find(sql, rowMapper, username);
    }
}
