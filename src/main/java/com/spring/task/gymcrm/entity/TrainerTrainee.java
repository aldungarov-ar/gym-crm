package com.spring.task.gymcrm.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "trainer_trainee")
public class TrainerTrainee {
    @EmbeddedId
    private TrainerTraineeId id = new TrainerTraineeId();

    @ManyToOne
    @MapsId("traineeId")
    @JoinColumn(name = "trainee_id")
    private Trainee trainee;

    @ManyToOne
    @MapsId("trainerId")
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @Embeddable
    public static class TrainerTraineeId implements Serializable {

        @Column(name = "trainee_id")
        private Integer traineeId;

        @Column(name = "trainer_id")
        private Integer trainerId;
    }
}
