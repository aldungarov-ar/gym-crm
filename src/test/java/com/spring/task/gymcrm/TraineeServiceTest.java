package com.spring.task.gymcrm;

import com.spring.task.gymcrm.entity.Trainee;
import com.spring.task.gymcrm.entity.User;
import com.spring.task.gymcrm.service.TraineeService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

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

    @Test
    void testCreateTraineeSuccess() {
        // given
        Trainee expectedTrainee = createExpectedTrainee();
        Trainee traineeCreateRequest = createExpectedTrainee();
        traineeCreateRequest.getUser().setUsername("");
    }

    Trainee createExpectedTrainee() {
        Trainee trainee = new Trainee();
        trainee.setUser(createExpectedUser());
        trainee.setAddress("London GB");
        return trainee;
    }

    User createExpectedUser() {
        User user = User.builder()
                .firstName("Ian")
                .lastName("Holm")
                .isActive(true)
                .build();
        user.setUsername("Ian.Holm");

        return user;
    }

    /*@Test
    void testCreateTraineeSuccess() {
        // given
        Trainee createTraineeRequest = createFixedTraineeUpdateRequest();
        com.spring.task.gymcrm.entity.Trainee expectedTrainee = createExpectedTrainee(createTraineeRequest);

        // when
        com.spring.task.gymcrm.entity.Trainee trainee = traineeService.create(createTraineeRequest);

        // then
        Assertions.assertNotNull(trainee);
        Assertions.assertEquals(expectedTrainee.getFirstName(), trainee.getFirstName());
        Assertions.assertEquals(expectedTrainee.getLastName(), trainee.getLastName());
        Assertions.assertEquals(expectedTrainee.getUsername(), trainee.getUsername());
        Assertions.assertEquals(expectedTrainee.isActive(), trainee.isActive());
        Assertions.assertEquals(expectedTrainee.getAddress(), trainee.getAddress());
        Assertions.assertEquals(expectedTrainee.getDateOfBirth(), trainee.getDateOfBirth());
    }*/
}
