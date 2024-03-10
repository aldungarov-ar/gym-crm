package com.spring.task.gymcrm.dto;

import com.spring.task.gymcrm.entity.Trainer;
import com.spring.task.gymcrm.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TrainerRs {
    private User user;
    private Trainer trainer;
}
