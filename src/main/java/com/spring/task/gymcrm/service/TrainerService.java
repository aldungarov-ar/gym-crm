package com.spring.task.gymcrm.service;

import com.spring.task.gymcrm.dao.TrainerDAO;
import com.spring.task.gymcrm.dao.UserDAO;
import com.spring.task.gymcrm.dto.TrainerRs;
import com.spring.task.gymcrm.dto.TrainerUpdateRq;
import com.spring.task.gymcrm.entity.Trainer;
import com.spring.task.gymcrm.entity.User;
import com.spring.task.gymcrm.exception.DBUpdateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@Service
public class TrainerService {
    private final TrainerDAO trainerDAO;
    private final UserDAO userDAO;
    private final UserService userService;

    @Autowired
    public TrainerService(TrainerDAO trainerDAO, UserDAO userDAO, UserService userService) {
        this.trainerDAO = trainerDAO;
        this.userDAO = userDAO;
        this.userService = userService;
    }

    public Trainer create(TrainerUpdateRq rq) throws DBUpdateException {
        User user = new User(rq.getFirstName(), rq.getLastName());
        user.setId(userService.save(user));

        Trainer trainer = new Trainer(user.getId(), rq.getSpecializationId());
        trainer.setId(trainerDAO.save(trainer));
        return trainer;
    }

    public Trainer update(TrainerUpdateRq rq) throws DBUpdateException {
        Trainer trainer = trainerDAO.findById(rq.getId());
        trainer.setSpecializationId(rq.getSpecializationId());

        userService.update(rq, trainer.getUserId());
        trainerDAO.update(trainer);

        return trainer;
    }

    public TrainerRs findById(Long id) {
        Trainer trainer = trainerDAO.findById(id);
        User user = userDAO.findById(trainer.getUserId());
        return new TrainerRs(user, trainer);
    }

    public List<TrainerRs> findAll() {
        List<Trainer> trainersList = trainerDAO.findAll();
        List<TrainerRs> trainersRs = new ArrayList<>();

        for (Trainer trainer : trainersList) {
            User user = userDAO.findById(trainer.getUserId());
            TrainerRs trainerRs = new TrainerRs(user, trainer);
            trainersRs.add(trainerRs);
        }

        return trainersRs;
    }
}