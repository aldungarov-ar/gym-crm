package com.spring.task.gymcrm.dao;


import com.spring.task.gymcrm.entity.Trainer;
import com.spring.task.gymcrm.exception.DBUpdateException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

@Repository
public class TrainerDAO extends BaseDAO {
    private final RowMapper<Trainer> rowMapper = (ResultSet rs, int rowNum) -> {
        Trainer trainer = new Trainer();
        trainer.setId(rs.getLong("id"));
        trainer.setSpecializationId(rs.getLong("specialization_id"));
        trainer.setUserId(rs.getLong("user_id"));
        return trainer;
    };

    public long save(Trainer trainer) throws DBUpdateException {
        String sql = "INSERT INTO trainers (specialization_id, user_id) VALUES (?, ?, ?)";
        return update(sql, trainer.getSpecializationId(), trainer.getUserId());
    }

    public void update(Trainer trainer) throws DBUpdateException {
        String sql = "UPDATE trainers SET specialization_id = ?";
        update(sql, trainer.getSpecializationId());
    }

    public Trainer findById(Long id) {
        String sql = "SELECT * FROM trainers WHERE id = ?";
        return find(sql, rowMapper, id);
    }

    public List<Trainer> findAll() {
        String sql = "SELECT * FROM trainers";
        return findAll(sql, rowMapper);
    }
}
