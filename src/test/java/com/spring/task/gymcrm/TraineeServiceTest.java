package com.spring.task.gymcrm;

import com.spring.task.gymcrm.dto.TraineeUpdateRequest;
import com.spring.task.gymcrm.dto.UserUpdateRequest;
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
        TraineeUpdateRequest createTraineeRequest = createFixedTraineeUpdateRequest();
        Trainee expectedTrainee = createExpectedTrainee(createTraineeRequest);

        // when
        Trainee trainee = traineeService.create(createTraineeRequest);

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
    static TraineeUpdateRequest createFixedTraineeUpdateRequest() {
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setFirstName("Samwise");
        userUpdateRequest.setLastName("Gamgee");
        userUpdateRequest.setUserIsActive(true);

        TraineeUpdateRequest traineeUpdateRequest = new TraineeUpdateRequest();
        traineeUpdateRequest.setUserUpdateRequest(userUpdateRequest);
        traineeUpdateRequest.setDateOfBirth(LocalDate.of(1975, 1, 1));
        traineeUpdateRequest.setAddress("Shire");
        return traineeUpdateRequest;
    }

    static Trainee createExpectedTrainee(TraineeUpdateRequest request) {
        Trainee trainee = new Trainee();
        trainee.setFirstName(request.getUserUpdateRequest().getFirstName());
        trainee.setLastName(request.getUserUpdateRequest().getLastName());
        trainee.setUsername(request.getUserUpdateRequest().getFirstName() + "." + request.getUserUpdateRequest().getLastName());
        trainee.setActive(request.getUserUpdateRequest().isUserIsActive());
        trainee.setAddress(request.getAddress());
        trainee.setDateOfBirth(request.getDateOfBirth());
        return trainee;
    }

    @Test
    void testCreateTraineeBirthDateFailure() {
        // given
        TraineeUpdateRequest expectedTrainee = createFixedTraineeUpdateRequest();
        expectedTrainee.setDateOfBirth(LocalDate.now().plusDays(1));

        // when
        Exception exception = Assertions.assertThrows(Exception.class, () -> traineeService.create(expectedTrainee));

        // then
        Assertions.assertNotNull(exception);
    }

    @Test
    void testCreateTraineeUserInfoValidationFailure() {
        // given
        TraineeUpdateRequest createTraineeRequest = createFixedTraineeUpdateRequest();
        createTraineeRequest.getUserUpdateRequest().setFirstName("");
        createTraineeRequest.getUserUpdateRequest().setLastName("");

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
        userUpdateRequest.setUserIsActive(false);
        TraineeUpdateRequest traineeCreateRequest = new TraineeUpdateRequest();
        traineeCreateRequest.setUserUpdateRequest(userUpdateRequest);
        traineeCreateRequest.setDateOfBirth(LocalDate.of(1971, 2, 25));
        traineeCreateRequest.setAddress("Santa Monica");

        Trainee createdTrainee = traineeService.create(traineeCreateRequest);

        // when
        TraineeUpdateRequest traineeUpdateRequest = createFixedTraineeUpdateRequest();
        traineeUpdateRequest.setUserId(createdTrainee.getId());
        Trainee updatedTrainee = traineeService.update(traineeUpdateRequest);

        // then
        Trainee expectedTrainee = createExpectedTrainee(traineeUpdateRequest);
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
        TraineeUpdateRequest createTraineeRequest = createFixedTraineeUpdateRequest();
        Trainee expectedTrainee = createExpectedTrainee(createTraineeRequest);
        Trainee createdTrainee = traineeService.create(createTraineeRequest);

        // when
        Trainee foundTrainee = traineeService.get(createdTrainee.getId());

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
        TraineeUpdateRequest createTraineeRequest = createFixedTraineeUpdateRequest();
        Trainee createdTrainee = traineeService.create(createTraineeRequest);

        // when
        boolean isDeleted = traineeService.delete(createdTrainee.getId());

        // then
        Assertions.assertTrue(isDeleted);
        Assertions.assertNull(traineeService.get(createdTrainee.getId()));
    }
}
