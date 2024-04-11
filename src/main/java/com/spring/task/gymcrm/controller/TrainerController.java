package com.spring.task.gymcrm.controller;

import com.spring.task.gymcrm.dto.TrainerDto;
import com.spring.task.gymcrm.entity.Trainer;
import com.spring.task.gymcrm.entity.mapper.TrainerMapper;
import com.spring.task.gymcrm.service.TrainerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/trainers")
public class TrainerController {
    private final TrainerService trainerService;
    private final TrainerMapper trainerMapper;

    @PostMapping("/register")
    public TrainerDto registerTrainer(@Valid TrainerDto trainerDto) {
        return trainerService.create(trainerDto);
    }

    @GetMapping("/{username}")
    public ResponseEntity<TrainerDto> getTrainerByUsername(@PathVariable @NotNull String username) {
        Optional<Trainer> optionalTrainer = trainerService.getByUsername(username);
        return optionalTrainer.map(trainer -> ResponseEntity.ok(trainerMapper.toDto(trainer)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{username}")
    public ResponseEntity<TrainerDto> updateTrainerByUsername(@PathVariable @NotNull String username,
                                                              @Valid TrainerDto trainerDto) {
        Optional<Trainer> optionalTrainer = trainerService.getByUsername(username);
        if (optionalTrainer.isPresent()) {
            Trainer updatedTrainer = trainerService.update(trainerDto);
            return ResponseEntity.ok(trainerMapper.toDto(updatedTrainer));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Object> deleteTrainer(@NotNull @PathVariable String username) {
        trainerService.deleteByUsername(username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/not-assign-to/{traineeUsername}")
    public ResponseEntity<List<TrainerDto>> getTrainersNotAssignToTrainee(@PathVariable @NotNull String traineeUsername) {
        List<Trainer> trainers = trainerService.getTrainersNotAssignToTrainee(traineeUsername);
        return ResponseEntity.ok(trainerMapper.toDtoList(trainers));
    }
}
