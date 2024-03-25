package com.spring.task.gymcrm.repository;

import com.spring.task.gymcrm.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class TrainingRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    private final GeneralTrainingRepository generalTrainingRepository;

    public List<Training> findByTrainerUsernameWithCriteria(String username, Map<CriteriaName, Object> criteriaMap) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Training> criteria = criteriaBuilder.createQuery(Training.class);
        Root<Training> training = criteria.from(Training.class);

        List<Predicate> predicates = new ArrayList<>();

        if (criteriaMap.containsKey(CriteriaName.FROM_DATE)) {
            Date dateFrom = (Date) criteriaMap.get(CriteriaName.FROM_DATE);
            predicates.add(criteriaBuilder.greaterThan(training.get("date"), dateFrom));
        }

        if (criteriaMap.containsKey(CriteriaName.TO_DATE)) {
            Date dateFrom = (Date) criteriaMap.get(CriteriaName.TO_DATE);
            predicates.add(criteriaBuilder.greaterThan(training.get("date"), dateFrom));
        }

        if (criteriaMap.containsKey(CriteriaName.TRAINEE_USERNAME)) {
            Join<Training, Trainee> trainee = training.join("trainee", JoinType.INNER);
            String traineeUsername = criteriaMap.get(CriteriaName.TRAINEE_USERNAME).toString();
            Join<Trainee, User> user = trainee.join("user", JoinType.INNER);
            predicates.add(criteriaBuilder.equal(user.get("username"), traineeUsername));
        }

        Join<Training, Trainer> trainer = training.join("trainer", JoinType.INNER);
        Join<Trainer, User> user = trainer.join("user", JoinType.INNER);
        predicates.add(criteriaBuilder.equal(user.get("username"), username));

        criteria.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(criteria).getResultList();
    }

    public List<Training> findByTraineeUsernameWithCriteria(String username, Map<CriteriaName, Object> criteriaMap) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Training> criteria = criteriaBuilder.createQuery(Training.class);
        Root<Training> training = criteria.from(Training.class);

        List<Predicate> predicates = new ArrayList<>();

        if (criteriaMap.containsKey(CriteriaName.FROM_DATE)) {
            Date dateFrom = (Date) criteriaMap.get(CriteriaName.FROM_DATE);
            predicates.add(criteriaBuilder.greaterThan(training.get("date"), dateFrom));
        }

        if (criteriaMap.containsKey(CriteriaName.TO_DATE)) {
            Date dateFrom = (Date) criteriaMap.get(CriteriaName.TO_DATE);
            predicates.add(criteriaBuilder.greaterThan(training.get("date"), dateFrom));
        }

        if (criteriaMap.containsKey(CriteriaName.TRAINER_USERNAME)) {
            Join<Training, Trainer> trainer = training.join("trainer", JoinType.INNER);
            String trainerUsername = criteriaMap.get(CriteriaName.TRAINEE_USERNAME).toString();
            Join<Trainer, User> user = trainer.join("user", JoinType.INNER);
            predicates.add(criteriaBuilder.equal(user.get("username"), trainerUsername));
        }

        if (criteriaMap.containsKey(CriteriaName.TRAINING_TYPE)) {
            Join<Training, TrainingType> trainingType = training.join("trainingType", JoinType.INNER);
            int trainingTypeId = Integer.parseInt(criteriaMap.get(CriteriaName.TRAINING_TYPE).toString());
            predicates.add(criteriaBuilder.equal(trainingType.get("id"), trainingTypeId));
        }

        Join<Training, Trainee> trainee = training.join("trainee", JoinType.INNER);
        Join<Trainer, User> user = trainee.join("user", JoinType.INNER);
        predicates.add(criteriaBuilder.equal(user.get("username"), username));

        criteria.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(criteria).getResultList();
    }

    public Training save(Training training) {
        return generalTrainingRepository.save(training);
    }

    public List<Training> findByTraineeUsername(String username) {
        return generalTrainingRepository.findByTrainee_User_UsernameIgnoreCase(username);
    }

    public List<Training> findByTrainerUsername(String username) {
        return generalTrainingRepository.findByTrainer_User_UsernameIgnoreCase(username);
    }
}
