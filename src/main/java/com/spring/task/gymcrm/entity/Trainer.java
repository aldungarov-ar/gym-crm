package com.spring.task.gymcrm.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Trainer extends User {
    private Long specializationId;

    public Trainer(User user) {
        super(user);
    }
}
