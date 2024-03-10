package com.spring.task.gymcrm;

import com.spring.task.gymcrm.dao.UserDAO;
import com.spring.task.gymcrm.dto.PasswordUpdateRq;
import com.spring.task.gymcrm.dto.TraineeUpdateRq;
import com.spring.task.gymcrm.dto.TrainerUpdateRq;
import com.spring.task.gymcrm.entity.User;
import com.spring.task.gymcrm.exception.DBUpdateException;
import com.spring.task.gymcrm.exception.SamePasswordUpdateException;
import com.spring.task.gymcrm.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("ALL")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserDAO userDAO;
    @InjectMocks
    UserService userService;

    User user;

    @BeforeEach
    void setUp() {
        user = new User("FName", "LName");
    }

    @Test
    void testSave_NewUser() throws DBUpdateException {
        when(userDAO.save(user)).thenReturn(1L);

        Long id = userService.save(user);

        Assertions.assertNotNull(id);
        Assertions.assertNotNull(user.getPassword());
        verify(userDAO).save(user);
    }

    @Test
    void whenUsernameExists_thenUsernameUpdated() throws DBUpdateException {
        String expectedUsername = "1_FName.LName";
        User existingUser = user;

        when(userDAO.findByUsername(existingUser.getUsername())).thenReturn(existingUser);
        when(userDAO.save(existingUser)).thenReturn(1L);

        userService.save(existingUser);

        Assertions.assertEquals(expectedUsername, existingUser.getUsername());
    }

    @Test
    void testUpdateUser_trainerUpdateRq_Success() throws DBUpdateException {
        User expectedUser = new User("NewFirstName", "NewLastName");
        expectedUser.setId(1L);
        expectedUser.setIsActive(true);

        TrainerUpdateRq trainerUpdateRq = new TrainerUpdateRq();
        trainerUpdateRq.setId(1L);
        trainerUpdateRq.setFirstName("NewFirstName");
        trainerUpdateRq.setLastName("NewLastName");
        trainerUpdateRq.setActive(true);

        when(userDAO.findById(1L)).thenReturn(user);

        userService.update(trainerUpdateRq, 1L);

        Assertions.assertEquals(expectedUser.getFirstName(), user.getFirstName());
        Assertions.assertEquals(expectedUser.getLastName(), user.getLastName());
        Assertions.assertEquals(expectedUser.getUsername(), user.getUsername());
        Assertions.assertEquals(expectedUser.getIsActive(), user.getIsActive());
    }

    @Test
    void testUpdateUser_traineeUpdateRq_Sucess() throws DBUpdateException {
        User expectedUser = new User("NewFirstName", "NewLastName");
        expectedUser.setId(1L);
        expectedUser.setIsActive(true);

        TraineeUpdateRq traineeUpdateRq = new TraineeUpdateRq();
        traineeUpdateRq.setId(1L);
        traineeUpdateRq.setFirstName("NewFirstName");
        traineeUpdateRq.setLastName("NewLastName");
        traineeUpdateRq.setActive(true);

        when(userDAO.findById(traineeUpdateRq.getId())).thenReturn(user);

        userService.update(traineeUpdateRq, 1L);

        Assertions.assertEquals(expectedUser.getFirstName(), user.getFirstName());
        Assertions.assertEquals(expectedUser.getLastName(), user.getLastName());
        Assertions.assertEquals(expectedUser.getUsername(), user.getUsername());
        Assertions.assertEquals(expectedUser.getIsActive(), user.getIsActive());
    }

    @Test
    void testUpdatePassword_success() throws DBUpdateException {
        PasswordUpdateRq passwordUpdateRq = new PasswordUpdateRq(1L, "SomeNewPassword");
        when(userDAO.findById(passwordUpdateRq.getUserId())).thenReturn(user);
        user.setPassword("oldPassword");

        Assertions.assertDoesNotThrow(() -> userService.updatePassword(passwordUpdateRq));
        verify(userDAO).save(user);
    }

    @Test
    void testUpdatePassword_failedSamePassword() throws DBUpdateException {
        PasswordUpdateRq passwordUpdateRq = new PasswordUpdateRq(1L, "oldPassword");
        when(userDAO.findById(passwordUpdateRq.getUserId())).thenReturn(user);
        user.setPassword("oldPassword");

        Assertions.assertThrows(SamePasswordUpdateException.class, () -> userService.updatePassword(passwordUpdateRq));
    }
}