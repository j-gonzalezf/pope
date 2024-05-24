/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.udc.fi.dc.tfg.model.entities;

import java.time.LocalDate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class TrainingCyclesTest {

    private TrainingCycles cycle;
    private TrainingCycles trainingCycle;
    private Users trainer;
    private Users client;
    private Users newTrainer;
    private Users newClient;

    @Before
    public void setUp() {
        cycle = new TrainingCycles();
        trainer = new Users("trainer@example.com", "password", "Test Trainer",
                "1234567890", null, null);
        client = new Users("client@example.com", "password", "Test Client",
                "1234567890", null, null, null, null, null, trainer);
        newTrainer = new Users("newTrainer@example.com", "password", "Test Trainer",
                "1234567890", null, null);
        newClient = new Users("newClient@example.com", "password", "Test Client",
                "1234567890", null, null, null, null, null, newTrainer);
        trainingCycle = new TrainingCycles("cycleName", null, LocalDate.of(2000, 1, 1),
                LocalDate.of(2001, 1, 1), trainer, client);
    }

    @Test
    public void testConstructors() {

        // Test cycle constructor
        assertNotNull(cycle);
        assertNull(cycle.getId());

        // Test training cycle constructor
        assertNotNull(trainingCycle);
        assertEquals("cycleName", trainingCycle.getName());
        assertEquals(LocalDate.of(2000, 1, 1), trainingCycle.getFromDate());
        assertEquals(LocalDate.of(2001, 1, 1), trainingCycle.getToDate());
        assertEquals(trainer, trainingCycle.getTrainer());
        assertEquals(client, trainingCycle.getClient());

    }

    @Test
    public void testGettersAndSetters() {

        // Test setters
        trainingCycle.setId(3L);
        trainingCycle.setName("newCycleName");
        trainingCycle.setDescription("cycleDescription");
        trainingCycle.setFromDate(LocalDate.of(2000, 2, 1));
        trainingCycle.setToDate(LocalDate.of(2001, 2, 1));
        trainingCycle.setTrainer(newTrainer);
        trainingCycle.setClient(newClient);

        // Test getters
        assertEquals(3L, trainingCycle.getId().longValue());
        assertEquals("newCycleName", trainingCycle.getName());
        assertEquals("cycleDescription", trainingCycle.getDescription());
        assertEquals(LocalDate.of(2000, 2, 1), trainingCycle.getFromDate());
        assertEquals(LocalDate.of(2001, 2, 1), trainingCycle.getToDate());
        assertEquals(newTrainer, trainingCycle.getTrainer());
        assertEquals(newClient, trainingCycle.getClient());

    }

}
