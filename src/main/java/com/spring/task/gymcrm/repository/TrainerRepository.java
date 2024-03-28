package com.spring.task.gymcrm.repository;

import com.spring.task.gymcrm.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {

    @Query("select t from Trainer t where t.user.username = ?1")
    Optional<Trainer> findByUsername(String username);

    @Query("select t from Trainer t inner join t.trainees trainees where upper(trainees.user.username) <> upper(?1)")
    List<Trainer> findTrainersNotAssignedByTraineeUsernameIgnoreCase(String traineeUsername);
}
