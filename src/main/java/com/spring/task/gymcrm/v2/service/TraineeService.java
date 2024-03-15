package com.spring.task.gymcrm.v2.service;

import com.spring.task.gymcrm.v2.dao.TraineeDAO;
import com.spring.task.gymcrm.v2.dto.TraineeUpdateRequest;
import com.spring.task.gymcrm.v2.entity.User;
import com.spring.task.gymcrm.v2.entity.Trainee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TraineeService {
    private final UserService userService;
    private final TraineeDAO traineeDAO;

    public Trainee create(TraineeUpdateRequest request) {
        User user = new User(request.getUserUpdateRequest().getFirstName(),
                request.getUserUpdateRequest().getLastName());
        Long userId = userService.save(user);
        user.setId(userId);

        Trainee trainee = new Trainee();
        trainee.setUser(user);
        trainee.setAddress(request.getAddress());
        trainee.setDateOfBirth(request.getDateOfBirth());
        Long traineeId = traineeDAO.save(trainee);
        return trainee;
    }
}
