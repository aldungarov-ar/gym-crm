package com.spring.task.gymcrm.repository;

import com.spring.task.gymcrm.entity.TrainerTrainee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerTraineeRepository extends JpaRepository<TrainerTrainee, Long> {
}
