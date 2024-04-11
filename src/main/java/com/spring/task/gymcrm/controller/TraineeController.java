package com.spring.task.gymcrm.controller;

import com.spring.task.gymcrm.dto.TraineeDto;
import com.spring.task.gymcrm.dto.TraineeTrainersUpdateRequest;
import com.spring.task.gymcrm.dto.TrainerDto;
import com.spring.task.gymcrm.service.TraineeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/trainees")
@Validated
public class TraineeController {
    private final TraineeService traineeService;

    @PostMapping
    public TraineeDto registerTrainee(@Valid TraineeDto traineeDto) {
        return traineeService.create(traineeDto);
    }

    @GetMapping("/{username}")
    public TraineeDto getTraineeByUsername(@PathVariable @NotNull String username) {
        return traineeService.getByUsername(username);
    }

    @PutMapping("/{username}")
    public TraineeDto updateTraineeByUsername(@PathVariable @NotNull String username,
                                              @Valid TraineeDto traineeDto) {
        return traineeService.getByUsername(username);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Object> deleteTrainee(@NotNull @PathVariable String username) {
        traineeService.deleteByUsername(username);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{username}/update-trainers")
    public List<TrainerDto> updateTraineeTrainers(@PathVariable @NotNull String username,
                                                  @Valid TraineeTrainersUpdateRequest trainerUsernames) {
        return traineeService.updateTrainers(trainerUsernames);
    }
}
