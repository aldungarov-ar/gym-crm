package com.spring.task.gymcrm.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "trainer_trainee")
public class TrainerTrainee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @MapsId("traineeId")
    @JoinColumn(name = "trainee_id")
    private Trainee trainee;

    @ManyToOne
    @MapsId("trainerId")
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;
}
