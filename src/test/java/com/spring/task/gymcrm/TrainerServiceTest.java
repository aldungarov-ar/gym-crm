package com.spring.task.gymcrm;

import com.spring.task.gymcrm.dao.TrainerDAO;
import com.spring.task.gymcrm.dao.UserDAO;
import com.spring.task.gymcrm.dto.TrainerRs;
import com.spring.task.gymcrm.dto.TrainerUpdateRq;
import com.spring.task.gymcrm.entity.Trainer;
import com.spring.task.gymcrm.entity.User;
import com.spring.task.gymcrm.exception.DBUpdateException;
import com.spring.task.gymcrm.service.TrainerService;
import com.spring.task.gymcrm.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private TrainerDAO trainerDAO;

    @Mock
    private UserDAO userDAO;

    @Mock
    private UserService userService;

    @InjectMocks
    private TrainerService trainerService;

    private TrainerUpdateRq trainerUpdateRq;
    private User user;
    private Trainer trainer;

    @BeforeEach
    void setUp() {
        user = new User("FirstName", "LastName");
        trainerUpdateRq = new TrainerUpdateRq("FirstName", "LastName", 1L, false);

        trainer = new Trainer(1L, 1L);
    }

    @Test
    void testCreateTrainer() throws DBUpdateException {
        when(userService.save(any(User.class))).thenReturn(1L);
        when(trainerDAO.save(any(Trainer.class))).thenReturn(1L);

        Trainer createdTrainer = trainerService.create(trainerUpdateRq);

        assertNotNull(createdTrainer);
        assertEquals(1L, createdTrainer.getId());
        verify(trainerDAO).save(any(Trainer.class));
    }

    @Test
    void testUpdateTrainer() throws DBUpdateException {
        when(trainerDAO.findById(trainerUpdateRq.getId())).thenReturn(trainer);

        Trainer updatedTrainer = trainerService.update(trainerUpdateRq);

        assertNotNull(updatedTrainer);
        assertEquals(trainerUpdateRq.getSpecializationId(), updatedTrainer.getSpecializationId());
        verify(trainerDAO).update(trainer);
    }

    @Test
    void testFindTrainerById() {
        when(trainerDAO.findById(1L)).thenReturn(trainer);
        when(userDAO.findById(trainer.getUserId())).thenReturn(user);

        TrainerRs foundTrainer = trainerService.findById(1L);

        assertNotNull(foundTrainer);
        assertEquals(trainer.getId(), foundTrainer.getTrainer().getId());
        assertEquals(user.getId(), foundTrainer.getUser().getId());
    }

    @Test
    void testFindAllTrainers() {
        List<Trainer> trainersList = new ArrayList<>();
        trainersList.add(trainer);

        when(trainerDAO.findAll()).thenReturn(trainersList);
        when(userDAO.findById(trainer.getUserId())).thenReturn(user);

        List<TrainerRs> trainersRsList = trainerService.findAll();

        assertFalse(trainersRsList.isEmpty());
        assertEquals(trainersList.size(), trainersRsList.size());
        assertEquals(trainer.getId(), trainersRsList.get(0).getTrainer().getId());
        assertEquals(user.getId(), trainersRsList.get(0).getUser().getId());
    }

    // Additional test methods as necessary...
}
