package com.spring.task.gymcrm;

import com.spring.task.gymcrm.dao.TrainingDAO;
import com.spring.task.gymcrm.dto.TrainingUpdateRq;
import com.spring.task.gymcrm.entity.Training;
import com.spring.task.gymcrm.exception.DBUpdateException;
import com.spring.task.gymcrm.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServicesTest {

    @Mock
    private TrainingDAO trainingDAO;

    @InjectMocks
    private TrainingService trainingServices;

    private TrainingUpdateRq trainingUpdateRq;
    private Training training;

    @BeforeEach
    void setUp() {
        trainingUpdateRq = new TrainingUpdateRq(1L, 1L, "UNLIMITED POWER", 1L, LocalDate.now(), Duration.ofMinutes(40));
        training = new Training(trainingUpdateRq);
    }

    @Test
    void testCreateTraining() throws DBUpdateException {
        when(trainingDAO.save(any(Training.class))).thenReturn(1L); // Assuming 1L is the ID of the saved training

        Training createdTraining = trainingServices.create(trainingUpdateRq);

        assertNotNull(createdTraining);
        assertEquals(1L, createdTraining.getId());
        verify(trainingDAO).save(any(Training.class));
    }

    @Test
    void testFindTrainingById() {
        when(trainingDAO.findById(1L)).thenReturn(training);

        Training foundTraining = trainingServices.findById(1L);

        assertNotNull(foundTraining);
        assertEquals(training.getId(), foundTraining.getId());
        verify(trainingDAO).findById(1L);
    }

    @Test
    void testFindAllTrainings() {
        List<Training> trainingsList = new ArrayList<>();
        trainingsList.add(training);

        when(trainingDAO.findAll()).thenReturn(trainingsList);

        List<Training> foundTrainings = trainingServices.findAll();

        assertFalse(foundTrainings.isEmpty());
        assertEquals(trainingsList.size(), foundTrainings.size());
        verify(trainingDAO).findAll();
    }
}

