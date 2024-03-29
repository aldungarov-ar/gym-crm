package com.spring.task.gymcrm;

import com.spring.task.gymcrm.entity.Trainee;
import com.spring.task.gymcrm.entity.User;
import com.spring.task.gymcrm.service.TraineeService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@SpringBootTest
class TraineeServiceTest {

    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres");
    @Autowired
    private TraineeService traineeService;

    @BeforeAll
    static void beforeAll() {
        container.withInitScript("init.sql")
                .start();
    }

    @AfterAll
    static void afterAll() {
        container.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    Trainee createTraineeCreateRequest() {
        Trainee trainee = new Trainee();
        trainee.setUser(createUserCreateRequest());
        trainee.setAddress("London GB");
        Calendar calendar = new GregorianCalendar(1931, Calendar.SEPTEMBER, 12);
        Date date = calendar.getTime();
        trainee.setDateOfBirth(date);
        return trainee;
    }

    User createUserCreateRequest() {
        User user = User.builder()
                .firstName("Ian")
                .lastName("Holm")
                .isActive(true)
                .build();
        user.setUsername("Ian.Holm");

        return user;
    }

    Trainee createUpdateTraineeRequest() {
        Trainee trainee = new Trainee();
        trainee.setUser(createUpdateUserRequest());
        trainee.setAddress("The Shire");
        Calendar calendar = new GregorianCalendar(2890, Calendar.SEPTEMBER, 22);
        Date date = calendar.getTime();
        trainee.setDateOfBirth(date);
        return trainee;
    }

    private User createUpdateUserRequest() {
        User user = User.builder()
                .firstName("Bilbo")
                .lastName("Baggins")
                .isActive(true)
                .build();
        user.setUsername("Bilbo.Baggins");

        return user;
    }

    @Test
    void testCreateTraineeSuccess() {
        // given
        Trainee expectedTrainee = createTraineeCreateRequest();
        Trainee traineeCreateRequest = createTraineeCreateRequest();
        traineeCreateRequest.getUser().setUsername("");

        // when
        Trainee createdTrainee = traineeService.create(traineeCreateRequest);

        // then
        Assertions.assertNotNull(createdTrainee);
        Assertions.assertEquals(1L, createdTrainee.getId());
        Assertions.assertEquals(expectedTrainee.getUser().getFirstName(), createdTrainee.getUser().getFirstName());
        Assertions.assertEquals(expectedTrainee.getUser().getLastName(), createdTrainee.getUser().getLastName());
        Assertions.assertEquals(createdTrainee.getUser().getFirstName() + "." + createdTrainee.getUser().getLastName(),
                createdTrainee.getUser().getUsername());
        Assertions.assertEquals(expectedTrainee.getUser().getIsActive(), createdTrainee.getUser().getIsActive());
        Assertions.assertEquals(expectedTrainee.getAddress(), createdTrainee.getAddress());
        Assertions.assertEquals(expectedTrainee.getDateOfBirth(), createdTrainee.getDateOfBirth());
    }

    /*@Test
    void testUpdateTraineeSuccess() {
        // given
        Trainee expectedTrainee = createUpdateTraineeRequest();
        Trainee traineeUpdateRequest = createUpdateTraineeRequest();
        traineeUpdateRequest.setId(1L);

        // when
        Trainee updatedTrainee = traineeService.update(traineeUpdateRequest);

        // then
        Assertions.assertNotNull(updatedTrainee);
        Assertions.assertEquals(expectedTrainee.getUser().getFirstName(), updatedTrainee.getUser().getFirstName());
        Assertions.assertEquals(expectedTrainee.getUser().getLastName(), updatedTrainee.getUser().getLastName());
        Assertions.assertEquals(updatedTrainee.getUser().getFirstName() + "." + updatedTrainee.getUser().getLastName(),
                updatedTrainee.getUser().getUsername());
        Assertions.assertEquals(expectedTrainee.getUser().getIsActive(), updatedTrainee.getUser().getIsActive());
        Assertions.assertEquals(expectedTrainee.getAddress(), updatedTrainee.getAddress());
        Assertions.assertEquals(expectedTrainee.getDateOfBirth(), updatedTrainee.getDateOfBirth());
    }*/
}
