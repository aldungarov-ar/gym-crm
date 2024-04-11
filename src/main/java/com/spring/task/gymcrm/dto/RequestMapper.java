package com.spring.task.gymcrm.dto;

import com.spring.task.gymcrm.repository.CriteriaName;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RequestMapper {
    public Map<CriteriaName, Object> toCriteriaMap(TrainingRequest request) {
        return Map.of(
                CriteriaName.TRAINEE_USERNAME, request.getTraineeUsername(),
                CriteriaName.FROM_DATE, request.getDateFrom(),
                CriteriaName.TO_DATE, request.getDateTo(),
                CriteriaName.TRAINER_USERNAME, request.getTrainerName(),
                CriteriaName.TRAINING_TYPE_ID, request.getTrainingTypeId()
        );
    }
}
