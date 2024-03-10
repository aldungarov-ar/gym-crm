package com.spring.task.gymcrm.dao;

import com.spring.task.gymcrm.entity.Trainee;
import com.spring.task.gymcrm.exception.DBUpdateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

@Repository
@Slf4j
public class TraineeDAO extends BaseDAO {
    private final RowMapper<Trainee> rowMapper = (ResultSet rs, int rowNum) -> {
        Trainee trainee = new Trainee();
        trainee.setId(rs.getLong("id"));
        trainee.setUserId(rs.getLong("user_id"));
        trainee.setAddress(rs.getString("address"));
        trainee.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
        return trainee;
    };

    public long save(Trainee trainee) throws DBUpdateException {
        String sql = "INSERT INTO trainees (user_id, address, date_of_birth) VALUES (?, ?, ?)";
        return update(sql, trainee.getUserId(), trainee.getAddress(), trainee.getDateOfBirth());
    }

    public void update(Trainee trainee) throws DBUpdateException {
        String sql = "UPDATE trainees SET address = ?, date_of_birth = ?";
        update(sql, trainee.getAddress(), trainee.getDateOfBirth());
    }

    public Trainee findById(Long id) {
        String sql = "SELECT * FROM trainees WHERE id = ?";
        return find(sql, rowMapper, id);
    }

    public List<Trainee> findAll() {
        String sql = "SELECT * FROM trainees";
        return findAll(sql, rowMapper);
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM trainees WHERE id = ?";
        return delete(sql, id);
    }
}
