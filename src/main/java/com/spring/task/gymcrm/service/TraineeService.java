package com.spring.task.gymcrm.service;

import com.spring.task.gymcrm.dao.TraineeDAO;
import com.spring.task.gymcrm.dao.UserDAO;
import com.spring.task.gymcrm.dto.TraineeRs;
import com.spring.task.gymcrm.dto.TraineeUpdateRq;
import com.spring.task.gymcrm.entity.Trainee;
import com.spring.task.gymcrm.entity.User;
import com.spring.task.gymcrm.exception.DBUpdateException;
import com.spring.task.gymcrm.exception.TraineeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@Service
@RequiredArgsConstructor
public class TraineeService {

    private final TraineeDAO traineeDAO;
    private final UserDAO userDAO;
    private final UserService userService;

      public Trainee create(TraineeUpdateRq rq) throws DBUpdateException {
        User user = new User(rq.getFirstName(), rq.getLastName());
        user.setId(userService.save(user));

        Trainee trainee = new Trainee(user.getId(), rq.getAddress(), rq.getDateOfBirth());
        trainee.setId(traineeDAO.save(trainee));
        return trainee;
    }

    public Trainee update(TraineeUpdateRq rq) throws DBUpdateException {
        Trainee trainee = traineeDAO.findById(rq.getId());
        trainee.setAddress(rq.getAddress());
        trainee.setDateOfBirth(rq.getDateOfBirth());

        userService.update(rq, trainee.getUserId());
        traineeDAO.update(trainee);

        return trainee;
    }

    public void deleteById(Long id) throws TraineeNotFoundException {
        int deleted = traineeDAO.deleteById(id);
        if (deleted < 1) {
            throw new TraineeNotFoundException("Failed to delete Trainee with id " + id + ". Trainee not found.");
        }
    }

    public TraineeRs findById(Long id) {
        Trainee trainee = traineeDAO.findById(id);
        User user = userDAO.findById(trainee.getUserId());
        return new TraineeRs(user, trainee);
    }

    public List<TraineeRs> findAll() {
        List<Trainee> traineesList = traineeDAO.findAll();
        List<TraineeRs> traineesRs = new ArrayList<>();

        for (Trainee trainee : traineesList) {
            User user = userDAO.findById(trainee.getUserId());
            TraineeRs traineeRs = new TraineeRs(user, trainee);
            traineesRs.add(traineeRs);
        }

        return traineesRs;
    }
}
