package com.spring.task.gymcrm.controller;

import com.spring.task.gymcrm.dto.RequestMapper;
import com.spring.task.gymcrm.dto.TrainingDto;
import com.spring.task.gymcrm.dto.TrainingsRequest;
import com.spring.task.gymcrm.entity.Training;
import com.spring.task.gymcrm.entity.mapper.TrainingMapper;
import com.spring.task.gymcrm.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/trainings")
public class TrainingController {
    private final TrainingService trainingService;
    private final RequestMapper requestMapper;
    private final TrainingMapper trainingMapper;

    @GetMapping
    public List<TrainingDto> getTrainingsList(TrainingsRequest request) {
        List<Training> trainings = trainingService.findByTraineeUsernameWithCriteria(request.getTraineeUsername(),
                requestMapper.toCriteriaMap(request));
        return trainingMapper.toDtoList(trainings);
    }
}
