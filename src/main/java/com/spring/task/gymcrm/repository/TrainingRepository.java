package com.spring.task.gymcrm.repository;

import com.spring.task.gymcrm.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {

    @Query("select t from Training t where upper(t.trainee.user.username) = upper(?1)")
    List<Training> findByTrainee_User_UsernameIgnoreCase(String username);

    @Query("select t from Training t where upper(t.trainer.user.username) = upper(?1)")
    List<Training> findByTrainer_User_UsernameIgnoreCase(String username);
}
