package com.spring.task.gymcrm.service;

import com.spring.task.gymcrm.dao.TrainingTypeDAO;
import com.spring.task.gymcrm.entity.TrainingType;
import com.spring.task.gymcrm.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainingTypeService {
    private final TrainingTypeDAO trainingTypeDAO;

    public TrainingType get(Long id) {
        TrainingType trainingType = trainingTypeDAO.findById(id);
        if (trainingType == null) {
            log.error("No training type found with ID: {}.", id);
            throw new EntityNotFoundException("No training type found with ID: " + id);
        }

        return trainingType;
    }
}
