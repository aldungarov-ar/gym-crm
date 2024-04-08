package com.spring.task.gymcrm;

import com.spring.task.gymcrm.dto.PasswordChangeRequest;
import com.spring.task.gymcrm.dto.TraineeDto;
import com.spring.task.gymcrm.dto.UserDto;
import com.spring.task.gymcrm.entity.Trainee;
import com.spring.task.gymcrm.entity.User;
import com.spring.task.gymcrm.service.TraineeService;
import org.jetbrains.annotations.NotNull;
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

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@SpringBootTest
class TraineeServiceTest {

    static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres");
    @Autowired
    private TraineeService traineeService;

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

    User createUser() {
        return User.builder()
                .firstName("Ian")
                .lastName("Holm")
                .username("Ian.Holm")
                .isActive(false)
                .build();
    }

    TraineeDto createTraineeDto() {
        Calendar calendar = new GregorianCalendar(1931, Calendar.SEPTEMBER, 12);
        Date date = calendar.getTime();

        return TraineeDto.builder()
                .userDto(createUserDto())
                .address("London GB")
                .dateOfBirth(date)
                .build();
    }

    private UserDto createUserDto() {
        return UserDto.builder()
                .firstName("Ian")
                .lastName("Holm")
                .isActive(false)
                .build();
    }

    PasswordChangeRequest createPasswordChangeRequest(Trainee trainee) {
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
        passwordChangeRequest.setUsername(trainee.getUser().getUsername());
        passwordChangeRequest.setNewPassword("newPasswordThatMatchesRequirements");
        return passwordChangeRequest;
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
    void testGetByIdSuccess() {
        TraineeDto traineeDto = createTraineeDto();
        Trainee expectedTrainee = createTrainee();

        Trainee createdTrainee = traineeService.create(traineeDto);
        expectedTrainee.setId(createdTrainee.getId());
        Trainee foundTrainee = traineeService.getById(expectedTrainee.getId()).get();

        assertNotNull(foundTrainee);
        assertEquals(expectedTrainee, foundTrainee);
    }

    @Test
    void testGetByUsernameSuccess() {
        TraineeDto traineeDto = createTraineeDto();
        Trainee expectedTrainee = createTrainee();

        Trainee createadTrainee = traineeService.create(traineeDto);
        expectedTrainee.setId(createadTrainee.getId());

        Trainee foundTrainee = traineeService.getByUsername(expectedTrainee.getUser().getUsername()).get();

        assertNotNull(foundTrainee);
        assertEquals(expectedTrainee, foundTrainee);
    }

    @Test
    void testUpdateSuccess() {
        TraineeDto initialTraineeDtoToUpdate = createTraineeDto();
        TraineeDto updateTraineeDto = createTraineeDtoForUpdate();
        Trainee expectedTrainee = createTrainee();

        Trainee createdTrainee = traineeService.create(initialTraineeDtoToUpdate);
        expectedTrainee.getUser().setId(createdTrainee.getUser().getId());
        expectedTrainee.setId(createdTrainee.getId());
        updateTraineeDto.getUserDto().setId(createdTrainee.getUser().getId());
        updateTraineeDto.setId(createdTrainee.getId());

        Trainee updatedTrainee = traineeService.update(updateTraineeDto);

        assertNotNull(updatedTrainee);
        assertEquals(expectedTrainee, updatedTrainee);
    }

    @NotNull
    private static TraineeDto createTraineeDtoForUpdate() {
        UserDto userDto = UserDto.builder()
                .firstName("Bilbo")
                .lastName("Baggins")
                .isActive(false)
                .build();

        Calendar calendar = new GregorianCalendar(1890, Calendar.SEPTEMBER, 22);
        Date date = calendar.getTime();

        return TraineeDto.builder()
                .userDto(userDto)
                .address("Shire")
                .dateOfBirth(date)
                .build();
    }

    @Test
    void testDeleteSuccess() {
        TraineeDto traineeDto = createTraineeDto();
        Trainee createdTrainee = traineeService.create(traineeDto);

        traineeService.deleteById(createdTrainee.getId());

        assertTrue(traineeService.getById(createdTrainee.getId()).isEmpty());
    }

    @Test
    void testActivateSuccess() {
        TraineeDto traineeDto = createTraineeDto();
        traineeDto.getUserDto().setIsActive(false);
        Trainee createdTrainee = traineeService.create(traineeDto);

        assertFalse(createdTrainee.getUser().getIsActive());

        traineeService.activate(createdTrainee.getId());

        Trainee activatedTrainee = traineeService.getById(createdTrainee.getId()).get();
        assertTrue(activatedTrainee.getUser().getIsActive());

        traineeService.activate(createdTrainee.getId());

        Trainee repeatedlyActivatedTrainee = traineeService.getById(createdTrainee.getId()).get();
        assertTrue(repeatedlyActivatedTrainee.getUser().getIsActive());
    }

    @Test
    void deActivateSuccess() {
        TraineeDto traineeDto = createTraineeDto();
        traineeDto.getUserDto().setIsActive(true);
        Trainee createdTrainee = traineeService.create(traineeDto);

        assertTrue(createdTrainee.getUser().getIsActive());

        traineeService.deActivate(createdTrainee.getId());

        Trainee deactivatedTrainee = traineeService.getById(createdTrainee.getId()).get();
        assertFalse(deactivatedTrainee.getUser().getIsActive());

        traineeService.deActivate(createdTrainee.getId());

        Trainee repeatedlyDeactivatedTrainee = traineeService.getById(createdTrainee.getId()).get();
        assertFalse(repeatedlyDeactivatedTrainee.getUser().getIsActive());
    }

    @Test
    void changePasswordSuccess() {
        TraineeDto traineeDto = createTraineeDto();
        Trainee createdTrainee = traineeService.create(traineeDto);
        PasswordChangeRequest passwordChangeRequest = createPasswordChangeRequest(createdTrainee);

        String newPassword = "newPasswordThatMatchesRequirements";
        traineeService.changePassword(passwordChangeRequest);

        Trainee updatedTrainee = traineeService.getById(createdTrainee.getId()).get();
        assertEquals(newPassword, updatedTrainee.getUser().getPassword());
    }
}
