package com.spring.task.gymcrm.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TrainerUpdateRq {
    private long id;
    private String firstName;
    private String lastName;
    private Long specializationId;
    private boolean isActive;

    public TrainerUpdateRq(String firstName, String lastName, Long specializationId, boolean isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.specializationId = specializationId;
        this.isActive = isActive;
    }
}
