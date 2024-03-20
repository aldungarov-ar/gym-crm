package com.spring.task.gymcrm.entity;

import jakarta.persistence.*;

import java.util.Date;


@Entity
@Table(name = "trainings")
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trainee_id")
    private Trainee trainee;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @Column(nullable = false)
    private String trainingName;

    @ManyToOne
    @JoinColumn(name = "training_type_id")
    private TrainingType trainingType;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date trainingDate;

    @Column(nullable = false)
    private Integer trainingDuration;
}
