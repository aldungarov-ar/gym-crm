package com.spring.task.gymcrm.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class TrainerUpdateRq {
    private long id;
    private String firstName;
    private String lastName;
    private Long specializationId;
    private boolean isActive;

}
