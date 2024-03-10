package com.spring.task.gymcrm.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Trainer {
    private Long id;
    private Long userId;
    private Long specializationId;

    public Trainer(Long userId, Long specializationId) {
        this.userId = userId;
        this.specializationId = specializationId;
    }
}
