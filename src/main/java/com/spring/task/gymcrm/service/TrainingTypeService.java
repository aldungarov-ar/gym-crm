package com.spring.task.gymcrm.service;

import com.spring.task.gymcrm.entity.TrainingType;
import com.spring.task.gymcrm.repository.TrainingTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainingTypeService {
    private final TrainingTypeRepository trainingTypeRepository;

    public TrainingType get(Long id) {
        return trainingTypeRepository.findById(id).orElse(null);
    }
}
