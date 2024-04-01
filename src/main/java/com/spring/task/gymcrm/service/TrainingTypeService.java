package com.spring.task.gymcrm.service;

import com.spring.task.gymcrm.entity.TrainingType;
import com.spring.task.gymcrm.repository.TrainingTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainingTypeService {
    private final TrainingTypeRepository trainingTypeRepository;

    public Optional<TrainingType> get(long id) {
        return trainingTypeRepository.findById(id);
    }
}
