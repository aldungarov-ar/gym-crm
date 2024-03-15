package com.spring.task.gymcrm.v2.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Trainer {
    private Long id;
    private Long specializationId;
    private User user;
}
