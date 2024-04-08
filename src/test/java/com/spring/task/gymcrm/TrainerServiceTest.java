package com.spring.task.gymcrm;

import com.spring.task.gymcrm.dto.PasswordChangeRequest;
import com.spring.task.gymcrm.dto.TrainerDto;
import com.spring.task.gymcrm.dto.UserDto;
import com.spring.task.gymcrm.entity.Trainer;
import com.spring.task.gymcrm.entity.User;
import com.spring.task.gymcrm.repository.TrainerRepository;
import com.spring.task.gymcrm.service.TrainerService;
import com.spring.task.gymcrm.service.TrainingTypeService;
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
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@SpringBootTest
public class TrainerServiceTest {

    static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres");
    @Autowired
    private TrainerService trainerService;
    @Autowired
    private TrainingTypeService trainingTypeService;

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
        JdbcDatabaseDelegate delegate = new JdbcDatabaseDelegate(container, "");
        ScriptUtils.runInitScript(delegate, "trainer_service_test_data.sql");
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

    Trainer createTrainer() {
        Trainer trainer = new Trainer();
        trainer.setUser(createUser());
        trainer.setSpecialization(trainingTypeService.get(1L).get());
        return trainer;
    }

    User createUser() {
        return User.builder()
                .firstName("Ian")
                .lastName("Holm")
                .username("Ian.Holm")
                .isActive(false)
                .build();
    }

    TrainerDto createTrainerDto() {
        return TrainerDto.builder()
                .userDto(createUserDto())
                .specializationId(1L)
                .build();
    }

    private UserDto createUserDto() {
        return UserDto.builder()
                .firstName("Ian")
                .lastName("Holm")
                .isActive(false)
                .build();
    }

    PasswordChangeRequest createPasswordChangeRequest(Trainer trainer) {
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
        passwordChangeRequest.setUsername(trainer.getUser().getUsername());
        passwordChangeRequest.setNewPassword("newPasswordThatMatchesRequirements");
        return passwordChangeRequest;
    }

    @Test
    void testCreateSuccess() {
        TrainerDto trainerDto = createTrainerDto();
        Trainer expectedTrainer = createTrainer();
        expectedTrainer.setId(1L);

        Trainer createdTrainer = trainerService.create(trainerDto);

        assertNotNull(createdTrainer);
        assertEquals(expectedTrainer, createdTrainer);
    }

    @Test
    void testGetByIdSuccess() {
        TrainerDto trainerDto = createTrainerDto();
        Trainer expectedTrainer = createTrainer();

        Trainer createdTrainer = trainerService.create(trainerDto);
        expectedTrainer.setId(createdTrainer.getId());
        Trainer foundTrainer = trainerService.getById(expectedTrainer.getId()).get();

        assertNotNull(foundTrainer);
        assertEquals(expectedTrainer, foundTrainer);
    }

    @Test
    void testGetByUsernameSuccess() {
        TrainerDto trainerDto = createTrainerDto();
        Trainer expectedTrainer = createTrainer();

        Trainer createadTrainer = trainerService.create(trainerDto);
        expectedTrainer.setId(createadTrainer.getId());

        Trainer foundTrainer = trainerService.getByUsername(expectedTrainer.getUser().getUsername()).get();

        assertNotNull(foundTrainer);
        assertEquals(expectedTrainer, foundTrainer);
    }

    @Test
    void testUpdateSuccess() {
        TrainerDto initialTrainerDtoToUpdate = createTrainerDto();
        TrainerDto updateTrainerDto = createTrainerDtoForUpdate();
        Trainer expectedTrainer = createTrainer();

        Trainer createdTrainer = trainerService.create(initialTrainerDtoToUpdate);
        expectedTrainer.getUser().setId(createdTrainer.getUser().getId());
        expectedTrainer.setId(createdTrainer.getId());
        updateTrainerDto.getUserDto().setId(createdTrainer.getUser().getId());
        updateTrainerDto.setId(createdTrainer.getId());

        Trainer updatedTrainer = trainerService.update(updateTrainerDto);

        assertNotNull(updatedTrainer);
        assertEquals(expectedTrainer, updatedTrainer);
    }

    @NotNull
    private static TrainerDto createTrainerDtoForUpdate() {
        UserDto userDto = UserDto.builder()
                .firstName("Bilbo")
                .lastName("Baggins")
                .isActive(false)
                .build();

        return TrainerDto.builder()
                .userDto(userDto)
                .specializationId(2L)
                .build();
    }

    @Test
    void testDeleteSuccess() {
        TrainerDto trainerDto = createTrainerDto();
        Trainer createdTrainer = trainerService.create(trainerDto);

        trainerService.deleteById(createdTrainer.getId());

        assertTrue(trainerService.getById(createdTrainer.getId()).isEmpty());
    }

    @Test
    void testActivateSuccess() {
        TrainerDto trainerDto = createTrainerDto();
        trainerDto.getUserDto().setIsActive(false);
        Trainer createdTrainer = trainerService.create(trainerDto);

        assertFalse(createdTrainer.getUser().getIsActive());

        trainerService.activate(createdTrainer.getId());

        Trainer activatedTrainer = trainerService.getById(createdTrainer.getId()).get();
        assertTrue(activatedTrainer.getUser().getIsActive());

        trainerService.activate(createdTrainer.getId());

        Trainer repeatedlyActivatedTrainer = trainerService.getById(createdTrainer.getId()).get();
        assertTrue(repeatedlyActivatedTrainer.getUser().getIsActive());
    }

    @Test
    void deActivateSuccess() {
        TrainerDto trainerDto = createTrainerDto();
        trainerDto.getUserDto().setIsActive(true);
        Trainer createdTrainer = trainerService.create(trainerDto);

        assertTrue(createdTrainer.getUser().getIsActive());

        trainerService.deActivate(createdTrainer.getId());

        Trainer deactivatedTrainer = trainerService.getById(createdTrainer.getId()).get();
        assertFalse(deactivatedTrainer.getUser().getIsActive());

        trainerService.deActivate(createdTrainer.getId());

        Trainer repeatedlyDeactivatedTrainer = trainerService.getById(createdTrainer.getId()).get();
        assertFalse(repeatedlyDeactivatedTrainer.getUser().getIsActive());
    }

    @Test
    void changePasswordSuccess() {
        TrainerDto trainerDto = createTrainerDto();
        Trainer createdTrainer = trainerService.create(trainerDto);
        PasswordChangeRequest passwordChangeRequest = createPasswordChangeRequest(createdTrainer);

        String newPassword = "newPasswordThatMatchesRequirements";
        trainerService.changePassword(passwordChangeRequest);

        Trainer updatedTrainer = trainerService.getById(createdTrainer.getId()).get();
        assertEquals(newPassword, updatedTrainer.getUser().getPassword());
    }

    @Autowired
    TrainerRepository trainerRepository;

    @Test
    void getTrainersNotAssignToTraineeSuccess() {
        TrainerDto trainerDto = createTrainerDto();
        Trainer createdTrainer = trainerService.create(trainerDto);

        assertTrue(trainerService.getTrainersNotAssignToTrainee("Frodo.Baggins").contains(createdTrainer));
    }
}
