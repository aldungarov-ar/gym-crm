package com.spring.task.gymcrm.service;

import com.spring.task.gymcrm.dao.TrainingDAO;
import com.spring.task.gymcrm.dto.TrainingUpdateRq;
import com.spring.task.gymcrm.entity.Training;
import com.spring.task.gymcrm.exception.DBUpdateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressWarnings("unused")
@Service
public class TrainingService {
    private final TrainingDAO trainingDAO;

    @Autowired
    public TrainingService(TrainingDAO trainingDAO) {
        this.trainingDAO = trainingDAO;
    }

    public Training create(TrainingUpdateRq rq) throws DBUpdateException {
        Training training = new Training(rq);
        training.setId(trainingDAO.save(training));
        return training;
    }

    public Training findById(Long id) {
        return trainingDAO.findById(id);
    }

    public List<Training> findAll() {
        return trainingDAO.findAll();
    }
}
