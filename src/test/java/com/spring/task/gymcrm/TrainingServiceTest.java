package com.spring.task.gymcrm;

import com.spring.task.gymcrm.dto.TrainingDto;
import com.spring.task.gymcrm.entity.Training;
import com.spring.task.gymcrm.repository.CriteriaName;
import com.spring.task.gymcrm.service.TrainingService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class TrainingServiceTest {

    static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres");
    @Autowired
    private TrainingService trainingService;

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

    /*@AfterEach
    void clearDb() throws IOException, InterruptedException {
        container.execInContainer("psql", "-U", container.getUsername(),
                "-d", container.getDatabaseName(), "-c", "TRUNCATE TABLE users CASCADE;");
    }*/

    TrainingDto createTrainingDto() {
        return TrainingDto.builder()
                        .traineeId(1000L)
                        .trainerId(2000L)
                        .trainingDate(getDefaultTrainigDate())
                        .trainingTypeId(10L)
                        .trainingName("Run hobbits!")
                        .trainingDuration(60)
                        .build();
    }

    @NotNull
    private static Date getDefaultTrainigDate() {
        GregorianCalendar calendar = new GregorianCalendar(2021, Calendar.NOVEMBER, 10);
        return Date.from(calendar.toZonedDateTime().toInstant());
    }

    Map<CriteriaName, Object> createFindCriteriaMap() {
        Map<CriteriaName, Object> criteriaMap = new HashMap<>();
        LocalDateTime localDateTimeTrainingFrom = LocalDateTime.ofInstant(getDefaultTrainigDate().toInstant(), ZoneId.systemDefault());
        Date trainingDateFrom = Date.from(localDateTimeTrainingFrom.minusMonths(1).atZone(ZoneId.systemDefault()).toInstant());
        criteriaMap.put(CriteriaName.FROM_DATE, trainingDateFrom);
        criteriaMap.put(CriteriaName.TO_DATE, new Date());
        criteriaMap.put(CriteriaName.TRAINING_TYPE_ID, 10L);
        return criteriaMap;
    }

    @Test
    void testCreateTrainingSuccess() {
        TrainingDto trainingDto = createTrainingDto();

        Training training = trainingService.create(trainingDto);

        assertNotNull(training);
    }

    @Test
    void testFindByTraineeUsername() {
        assertNotNull(trainingService.findByTraineeUsername("Bilbo.Baggins"));
    }

    @Test
    void testFindByTraineeUsernameWithCriteria() {
        assertNotNull(trainingService.findByTraineeUsernameWithCriteria("Bilbo.Baggins", createFindCriteriaMap()));
    }

    @Test
    void testFindByTrainerUsername() {
        assertNotNull(trainingService.findByTrainerUsername("Gandlaf.TheGray"));
    }

    @Test
    void testFindByTrainerUsernameWithCriteria() {
        assertNotNull(trainingService.findByTrainerUsernameWithCriteria("Aragorn.SonOfArathorn", createFindCriteriaMap()));
    }
}
