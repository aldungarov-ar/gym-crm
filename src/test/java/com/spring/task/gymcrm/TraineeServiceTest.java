package com.spring.task.gymcrm;

import com.spring.task.gymcrm.entity.Trainee;
import com.spring.task.gymcrm.service.TraineeService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDate;

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
    }

    @NotNull
    static Trainee createFixedTraineeUpdateRequest() {
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setFirstName("Samwise");
        userUpdateRequest.setLastName("Gamgee");
        userUpdateRequest.setActiveUser(true);

        Trainee trainee = new Trainee();
        trainee.setUser(userUpdateRequest);
        trainee.setDateOfBirth(LocalDate.of(1975, 1, 1));
        trainee.setAddress("Shire");
        return trainee;
    }

    static com.spring.task.gymcrm.entity.Trainee createExpectedTrainee(Trainee request) {
        com.spring.task.gymcrm.entity.Trainee trainee = new com.spring.task.gymcrm.entity.Trainee();
        trainee.setFirstName(request.getUser().getFirstName());
        trainee.setLastName(request.getUser().getLastName());
        trainee.setUsername(request.getUser().getFirstName() + "." + request.getUser().getLastName());
        trainee.setActive(request.getUser().isActiveUser());
        trainee.setAddress(request.getAddress());
        trainee.setDateOfBirth(request.getDateOfBirth());
        return trainee;
    }

    @Test
    void testCreateTraineeBirthDateFailure() {
        // given
        Trainee expectedTrainee = createFixedTraineeUpdateRequest();
        expectedTrainee.setDateOfBirth(LocalDate.now().plusDays(1));

        // when
        Exception exception = Assertions.assertThrows(Exception.class, () -> traineeService.create(expectedTrainee));

        // then
        Assertions.assertNotNull(exception);
    }

    @Test
    void testCreateTraineeUserInfoValidationFailure() {
        // given
        Trainee createTraineeRequest = createFixedTraineeUpdateRequest();
        createTraineeRequest.getUser().setFirstName("");
        createTraineeRequest.getUser().setLastName("");

        // when
        Exception exception = Assertions.assertThrows(Exception.class, () -> traineeService.create(createTraineeRequest));

        // then
        Assertions.assertNotNull(exception);
    }

    @Test
    void updateTraineeSuccess() {
        // given
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setFirstName("Sean");
        userUpdateRequest.setLastName("Astin");
        userUpdateRequest.setActiveUser(false);
        Trainee traineeCreateRequest = new Trainee();
        traineeCreateRequest.setUser(userUpdateRequest);
        traineeCreateRequest.setDateOfBirth(LocalDate.of(1971, 2, 25));
        traineeCreateRequest.setAddress("Santa Monica");

        com.spring.task.gymcrm.entity.Trainee createdTrainee = traineeService.create(traineeCreateRequest);

        // when
        Trainee trainee = createFixedTraineeUpdateRequest();
        trainee.setId(createdTrainee.getId());
        com.spring.task.gymcrm.entity.Trainee updatedTrainee = traineeService.update(trainee);

        // then
        com.spring.task.gymcrm.entity.Trainee expectedTrainee = createExpectedTrainee(trainee);
        Assertions.assertNotNull(updatedTrainee);
        Assertions.assertEquals(createdTrainee.getId(), updatedTrainee.getId());
        Assertions.assertEquals(expectedTrainee.getFirstName(), updatedTrainee.getFirstName());
        Assertions.assertEquals(expectedTrainee.getLastName(), updatedTrainee.getLastName());
        Assertions.assertEquals(expectedTrainee.getUsername(), updatedTrainee.getUsername());
        Assertions.assertEquals(expectedTrainee.isActive(), updatedTrainee.isActive());
        Assertions.assertEquals(expectedTrainee.getAddress(), updatedTrainee.getAddress());
        Assertions.assertEquals(expectedTrainee.getDateOfBirth(), updatedTrainee.getDateOfBirth());
    }

    @Test
    void findTraineeSuccess() {
        // given
        Trainee createTraineeRequest = createFixedTraineeUpdateRequest();
        com.spring.task.gymcrm.entity.Trainee expectedTrainee = createExpectedTrainee(createTraineeRequest);
        com.spring.task.gymcrm.entity.Trainee createdTrainee = traineeService.create(createTraineeRequest);

        // when
        com.spring.task.gymcrm.entity.Trainee foundTrainee = traineeService.get(createdTrainee.getId());

        // then
        Assertions.assertNotNull(foundTrainee);
        Assertions.assertEquals(expectedTrainee.getFirstName(), foundTrainee.getFirstName());
        Assertions.assertEquals(expectedTrainee.getLastName(), foundTrainee.getLastName());
        Assertions.assertEquals(expectedTrainee.getUsername(), foundTrainee.getUsername());
        Assertions.assertEquals(expectedTrainee.isActive(), foundTrainee.isActive());
        Assertions.assertEquals(expectedTrainee.getAddress(), foundTrainee.getAddress());
        Assertions.assertEquals(expectedTrainee.getDateOfBirth(), foundTrainee.getDateOfBirth());
    }

    @Test
    void deleteTraineeSuccess() {
        // given
        Trainee createTraineeRequest = createFixedTraineeUpdateRequest();
        Trainee createdTrainee = traineeService.create(createTraineeRequest);

        // when
        traineeService.delete(createdTrainee);

        // then
        Assertions.assertNull(traineeService.get(createdTrainee.getId()));
    }
}
