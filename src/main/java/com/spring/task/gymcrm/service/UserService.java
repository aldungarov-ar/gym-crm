package com.spring.task.gymcrm.service;

import com.spring.task.gymcrm.dao.UserDAO;
import com.spring.task.gymcrm.dto.PasswordUpdateRq;
import com.spring.task.gymcrm.dto.TraineeUpdateRq;
import com.spring.task.gymcrm.dto.TrainerUpdateRq;
import com.spring.task.gymcrm.entity.User;
import com.spring.task.gymcrm.exception.DBUpdateException;
import com.spring.task.gymcrm.exception.SamePasswordUpdateException;
import com.spring.task.gymcrm.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@SuppressWarnings("unused")
@Service
public class UserService {
    private final UserDAO userDAO;

    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public Long save(User user) throws DBUpdateException {
        user.setPassword(PasswordUtils.generatePassword());
        Long id = userDAO.save(user);

        User existingUser = userDAO.findByUsername(user.getUsername());

        if (existingUser != null) {
            user.setUsername(id + "_" + user.getUsername());
            userDAO.update(user);
        }

        return id;
    }

    public void updatePassword(PasswordUpdateRq passwordUpdateRq) throws SamePasswordUpdateException, DBUpdateException {
        User user = userDAO.findById(passwordUpdateRq.getUserId());

        if (!user.getPassword().equals(passwordUpdateRq.getNewPassword())) {
            user.setPassword(passwordUpdateRq.getNewPassword());
            userDAO.save(user);
        } else {
            throw new SamePasswordUpdateException("Failed to update password. New password is the same as old one.");
        }
    }

    public void update(TrainerUpdateRq rq, Long id) throws DBUpdateException {
        User user = userDAO.findById(id);
        user.setFirstName(rq.getFirstName());
        user.setLastName(rq.getLastName());
        user.setIsActive(rq.isActive());

        User existingUser = userDAO.findByUsername(user.getUsername());

        if (existingUser != null) {
            user.setUsername(id + "_" + user.getUsername());
        }

        userDAO.update(user);
    }

    public void update(TraineeUpdateRq rq, Long id) throws DBUpdateException {
        User user = userDAO.findById(id);
        user.setFirstName(rq.getFirstName());
        user.setLastName(rq.getLastName());
        user.setIsActive(rq.isActive());

        User existingUser = userDAO.findByUsername(user.getUsername());

        if (existingUser != null) {
            user.setUsername(id + "_" + user.getUsername());
        }

        userDAO.update(user);
    }
}
