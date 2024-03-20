package com.spring.task.gymcrm.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@Entity
@Table(name = "trainers")
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "specialization_id")
    private TrainingType specialization;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToMany(mappedBy = "trainers")
    private Set<Trainee> trainees;
}
