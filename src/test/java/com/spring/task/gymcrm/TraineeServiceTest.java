package com.spring.task.gymcrm;

import com.spring.task.gymcrm.dto.TraineeDto;
import com.spring.task.gymcrm.dto.UserDto;
import com.spring.task.gymcrm.entity.Trainee;
import com.spring.task.gymcrm.entity.User;
import com.spring.task.gymcrm.service.TraineeServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@SpringBootTest
class TraineeServiceTest {

    static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres");
    @Autowired
    private TraineeServiceImpl traineeService;

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @BeforeAll
    static void beforeAll() {
        container.withInitScript("init.sql")
                .start();
    }

    @AfterAll
    static void afterAll() {
        container.stop();
    }

    @AfterEach
    void clearDb() throws IOException, InterruptedException {
        container.execInContainer("psql", "-U", container.getUsername(),
                "-d", container.getDatabaseName(), "-c", "TRUNCATE TABLE users CASCADE;");
    }

    Trainee createTrainee() {
        Trainee trainee = new Trainee();
        trainee.setUser(createUser());
        trainee.setAddress("London GB");
        Calendar calendar = new GregorianCalendar(1931, Calendar.SEPTEMBER, 12);
        Date date = calendar.getTime();
        trainee.setDateOfBirth(date);
        return trainee;
    }

    TraineeDto createTraineeDto() {
        TraineeDto traineeDto = new TraineeDto();
        traineeDto.setUserDto(createUserDto());
        traineeDto.setAddress("London GB");
        Calendar calendar = new GregorianCalendar(1931, Calendar.SEPTEMBER, 12);
        Date date = calendar.getTime();
        traineeDto.setDateOfBirth(date);
        return traineeDto;
    }

    private UserDto createUserDto() {
        UserDto userDto = new UserDto();
        userDto.setFirstName("Ian");
        userDto.setLastName("Holm");
        userDto.setIsActive(true);

        return userDto;
    }

    User createUser() {
        return User.builder()
                .firstName("Ian")
                .lastName("Holm")
                .username("Ian.Holm")
                .isActive(true)
                .build();
    }

    @Test
    void testCreateSuccess() {
        TraineeDto traineeDto = createTraineeDto();
        Trainee expectedTrainee = createTrainee();
        expectedTrainee.setId(1L);

        Trainee createdTrainee = traineeService.create(traineeDto);

        assertNotNull(createdTrainee);
        assertEquals(expectedTrainee, createdTrainee);
    }

    @Test
    void testGetSuccess() {
        TraineeDto traineeDto = createTraineeDto();
        Trainee expectedTrainee = createTrainee();
        expectedTrainee.setId(2L);

        traineeService.create(traineeDto);

        Trainee foundTrainee = traineeService.get(2L).get();

        assertNotNull(foundTrainee);
        assertEquals(expectedTrainee, foundTrainee);
    }
}
