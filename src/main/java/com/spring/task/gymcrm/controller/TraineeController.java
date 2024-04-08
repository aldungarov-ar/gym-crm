package com.spring.task.gymcrm.controller;

import com.spring.task.gymcrm.dto.RegistrationAnswer;
import com.spring.task.gymcrm.dto.TraineeDto;
import com.spring.task.gymcrm.dto.TrainerDto;
import com.spring.task.gymcrm.dto.TrainersListUpdateRequest;
import com.spring.task.gymcrm.entity.Trainee;
import com.spring.task.gymcrm.entity.Trainer;
import com.spring.task.gymcrm.entity.mapper.TraineeMapper;
import com.spring.task.gymcrm.entity.mapper.TrainerMapper;
import com.spring.task.gymcrm.service.TraineeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/trainees")
@Validated
public class TraineeController {
    private final TraineeService traineeService;
    private final TraineeMapper traineeMapper;
    private final TrainerMapper trainerMapper;

    @PostMapping("/register")
    public RegistrationAnswer registerTrainee(@Valid TraineeDto traineeDto) {
        Trainee createdTrainee = traineeService.create(traineeDto);
        return traineeMapper.toRegistrationAnswer(createdTrainee);
    }

    @GetMapping("/{username}")
    public ResponseEntity<TraineeDto> getTraineeByUsername(@PathVariable @NotNull String username) {
        Optional<Trainee> optionalTrainee = traineeService.getByUsername(username);
        return optionalTrainee.map(trainee -> ResponseEntity.ok(traineeMapper.toDto(trainee)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{username}")
    public ResponseEntity<TraineeDto> updateTraineeByUsername(@PathVariable @NotNull String username,
                                                              @Valid TraineeDto traineeDto) {
        Optional<Trainee> optionalTrainee = traineeService.getByUsername(username);
        if (optionalTrainee.isPresent()) {
            Trainee updatedTrainee = traineeService.update(traineeDto);
            return ResponseEntity.ok(traineeMapper.toDto(updatedTrainee));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Object> deleteTrainee(@NotNull @PathVariable String username) {
        traineeService.deleteByUsername(username);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{username}/trainers/update")
    public ResponseEntity<List<TrainerDto>> updateTraineeTrainers(@PathVariable @NotNull String username,
                                                                  @Valid TrainersListUpdateRequest trainerUsernames) {
        List<Trainer> trainers = traineeService.updateTrainersList(trainerUsernames);
        return ResponseEntity.ok(trainerMapper.toDtoList(trainers));
    }
}
