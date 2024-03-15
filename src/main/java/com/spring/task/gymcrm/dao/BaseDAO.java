package com.spring.task.gymcrm.dao;

import com.spring.task.gymcrm.exception.DBUpdateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Component
@Slf4j
public class BaseDAO {

    protected DataSource dataSource;
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    protected Long update(String sql, Object... args) {
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < args.length; i++) {
                statement.setString(i, args[i].toString());
            }

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating table failed, no rows affected.");
            }

            ResultSet keys = statement.getGeneratedKeys();

            if (keys.next()) return keys.getLong(1);
            else throw new SQLException("Failed to update entity. No ID obtained.");


        } catch (SQLException e) {
            throw new DBUpdateException(e);
        }
    }

    protected <T> T find(String sql, RowMapper<T> rowMapper, Object... args) {
        return jdbcTemplate.queryForObject(sql, rowMapper, args);
    }

    protected <T> List<T> findAll(String sql, RowMapper<T> rowMapper) {
        return jdbcTemplate.query(sql, rowMapper);
    }

    protected int delete(String sql, Object... args) {
        return jdbcTemplate.update(sql, args);
    }
}
