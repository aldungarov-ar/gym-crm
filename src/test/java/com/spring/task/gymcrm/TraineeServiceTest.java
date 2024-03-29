package com.spring.task.gymcrm;

import com.spring.task.gymcrm.entity.Trainee;
import com.spring.task.gymcrm.entity.User;
import com.spring.task.gymcrm.exception.EntityNotFoundException;
import com.spring.task.gymcrm.exception.RequestValidationException;
import com.spring.task.gymcrm.exception.UpdateRequestValidationException;
import com.spring.task.gymcrm.service.TraineeService;
import org.junit.jupiter.api.*;
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

    private Trainee trainee;

    @BeforeEach
    void setUp() {
        trainee = createTrainee();
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
        User user = User.builder()
                .firstName("Ian")
                .lastName("Holm")
                .isActive(true)
                .build();
        user.setUsername("Ian.Holm");

        return user;
    }

    @Test
    void testCreateSuccess() {
        Trainee createdTrainee = traineeService.create(trainee);
        assertNotNull(createdTrainee);
        assertNotNull(createdTrainee.getId());
        String createdTraineeFirstName = createdTrainee.getUser().getFirstName();
        String createdTraineeLastName = createdTrainee.getUser().getLastName();
        assertEquals(createdTraineeFirstName + "." + createdTraineeLastName,
                createdTrainee.getUser().getUsername());
    }

    @Test
    void testCreateFailure() {
        Trainee invalidTrainee = new Trainee();
        assertThrows(UpdateRequestValidationException.class, () -> traineeService.create(invalidTrainee));
    }

    @Test
    void testUpdateSuccess() {
        Trainee savedTrainee = traineeService.create(trainee);
        String expectedAddress = "The Shire";
        savedTrainee.setAddress(expectedAddress);
        Trainee updatedTrainee = traineeService.update(savedTrainee);
        assertEquals(expectedAddress, updatedTrainee.getAddress());
    }

    @Test
    void testUpdateFailure() {
        Trainee request = new Trainee();
        request.setId(-1L);
        assertThrows(UpdateRequestValidationException.class, () -> traineeService.update(request));
    }

    @Test
    void testGetByIdSuccess() {
        Trainee savedTrainee = traineeService.create(trainee);
        Trainee foundTrainee = traineeService.get(savedTrainee.getId()).get();
        assertNotNull(foundTrainee);
    }

    @Test
    void testGetByIdFailure() {
        assertThrows(EntityNotFoundException.class, () -> traineeService.get(-1L));
    }

    @Test
    void testGetByUsernameSuccess() {
        Trainee savedTrainee = traineeService.create(trainee);
        Trainee foundTrainee = traineeService.get(savedTrainee.getUser().getUsername()).get();
        assertNotNull(foundTrainee);
    }

    @Test
    void testGetByUsernameFailure() {
        assertThrows(RequestValidationException.class, () -> traineeService.get(""));
    }

    @Test
    void testDelete() {
        Trainee savedTrainee = traineeService.create(trainee);
        assertDoesNotThrow(() -> traineeService.delete(savedTrainee));
        assertNull(traineeService.get(savedTrainee.getId()));
    }

    @Test
    void testActivateSuccess() {
        Trainee savedTrainee = traineeService.create(trainee);
        assertDoesNotThrow(() -> traineeService.activate(savedTrainee.getId()));
        assertTrue(traineeService.get(savedTrainee.getId()).get().getUser().getIsActive());
    }

    @Test
    void testDeactivateSuccess() {
        Trainee savedTrainee = traineeService.create(trainee);
        assertDoesNotThrow(() -> traineeService.deActivate(savedTrainee.getId()));
        assertFalse(traineeService.get(savedTrainee.getId()).get().getUser().getIsActive());
    }


    /*Trainee createTraineeCreateRequest() {
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
    }*/

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
