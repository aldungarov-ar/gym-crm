package com.spring.task.gymcrm.dto;

import com.spring.task.gymcrm.entity.Trainee;
import com.spring.task.gymcrm.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TraineeRs {
    private User user;
    private Trainee trainee;
}
