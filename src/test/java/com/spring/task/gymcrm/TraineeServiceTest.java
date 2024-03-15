package com.spring.task.gymcrm;

import com.spring.task.gymcrm.dao.TraineeDAO;
import com.spring.task.gymcrm.dao.UserDAO;
import com.spring.task.gymcrm.dto.TraineeRs;
import com.spring.task.gymcrm.dto.TraineeUpdateRq;
import com.spring.task.gymcrm.entity.Trainee;
import com.spring.task.gymcrm.entity.User;
import com.spring.task.gymcrm.exception.DBUpdateException;
import com.spring.task.gymcrm.exception.TraineeNotFoundException;
import com.spring.task.gymcrm.service.TraineeService;
import com.spring.task.gymcrm.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock
    TraineeDAO traineeDAO;

    @Mock
    UserDAO userDAO;

    @Mock
    UserService userService;

    @InjectMocks
    TraineeService traineeService;

    TraineeUpdateRq traineeUpdateRq;
    User user;
    Trainee trainee;

    @BeforeEach
    void setUp() {
        user = new User("FirstName", "LastName");
        trainee = new Trainee(1L, "Tatooine", LocalDate.of(2012, 12, 1));
        traineeUpdateRq = new TraineeUpdateRq();
    }

    @Test
    void testCreateTrainee() {
        when(userService.save(any(User.class))).thenReturn(1L); // Assuming user is saved and ID is returned
        when(traineeDAO.save(any(Trainee.class))).thenReturn(1L);

        Trainee createdTrainee = traineeService.create(traineeUpdateRq);

        Assertions.assertNotNull(createdTrainee);
        Assertions.assertEquals(1L, createdTrainee.getId());
        verify(traineeDAO).save(any(Trainee.class));
    }

    @Test
    void testUpdateTrainee() throws DBUpdateException {
        when(traineeDAO.findById(traineeUpdateRq.getId())).thenReturn(trainee);

        Trainee updatedTrainee = traineeService.update(traineeUpdateRq);

        Assertions.assertNotNull(updatedTrainee);
        Assertions.assertEquals(traineeUpdateRq.getAddress(), updatedTrainee.getAddress());
        Assertions.assertEquals(traineeUpdateRq.getDateOfBirth(), updatedTrainee.getDateOfBirth());
        verify(traineeDAO).update(any(Trainee.class));
    }

    @Test
    void testDeleteTrainee() {
        when(traineeDAO.deleteById(1L)).thenReturn(1);

        Assertions.assertDoesNotThrow(() -> traineeService.deleteById(1L));
    }

    @Test
    void testDeleteTrainee_NotFound() {
        when(traineeDAO.deleteById(1L)).thenReturn(0);

        Assertions.assertThrows(TraineeNotFoundException.class, () -> traineeService.deleteById(1L));
    }

    @Test
    void testFindTraineeById() {
        when(traineeDAO.findById(1L)).thenReturn(trainee);
        when(userDAO.findById(trainee.getUserId())).thenReturn(user);

        TraineeRs foundTrainee = traineeService.findById(1L);

        Assertions.assertNotNull(foundTrainee);
        Assertions.assertEquals(trainee.getId(), foundTrainee.getTrainee().getId());
        Assertions.assertEquals(user.getId(), foundTrainee.getUser().getId());
    }

    @Test
    void testFindAllTrainees() {
        List<Trainee> traineesList = new ArrayList<>();
        traineesList.add(trainee);

        when(traineeDAO.findAll()).thenReturn(traineesList);
        when(userDAO.findById(trainee.getUserId())).thenReturn(user);

        List<TraineeRs> traineesRsList = traineeService.findAll();

        Assertions.assertFalse(traineesRsList.isEmpty());
        Assertions.assertEquals(traineesList.size(), traineesRsList.size());
        Assertions.assertEquals(trainee.getId(), traineesRsList.get(0).getTrainee().getId());
        Assertions.assertEquals(user.getId(), traineesRsList.get(0).getUser().getId());
    }

}
