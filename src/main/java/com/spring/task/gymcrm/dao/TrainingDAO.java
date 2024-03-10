package com.spring.task.gymcrm.dao;

import com.spring.task.gymcrm.entity.Training;
import com.spring.task.gymcrm.exception.DBUpdateException;
import com.spring.task.gymcrm.exception.SQLDurationParseException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.time.Duration;
import java.util.List;

@Repository
public class TrainingDAO extends BaseDAO {
    private final RowMapper<Training> rowMapper = (ResultSet rs, int rowNum) -> {
        Training training = new Training();
        training.setTraineeId(rs.getLong("trainee_id"));
        training.setTrainerId(rs.getLong("trainer_id"));
        training.setName(rs.getString("name"));
        training.setTrainingType(rs.getLong("type"));
        training.setDate(rs.getDate("date").toLocalDate());
        training.setDuration(parseDuration(rs.getObject("duration")));
        return training;
    };

    public long save(Training training) throws DBUpdateException {
        String sql = "INSERT INTO trainings (trainee_id, trainer_id, name, trainingType_id, date, duration) VALUES (?, ?, ?, ?, ?, ?)";
        return update(sql, training.getTraineeId(), training.getTrainerId(), training.getName(), training.getTrainingType(), training.getDate(), training.getDuration());
    }

    public Training findById(Long id) {
        String sql = "SELECT * FROM trainings WHERE id = ?";
        return find(sql, rowMapper, id);
    }

    public List<Training> findAll() {
        String sql = "SELECT * FROM trainings";
        return findAll(sql, rowMapper);
    }

    private Duration parseDuration(Object object) {
        if (object instanceof Duration duration) {
            return duration;
        } else {
            throw new SQLDurationParseException("Failed to parse duration from " + object);
        }
    }
}
