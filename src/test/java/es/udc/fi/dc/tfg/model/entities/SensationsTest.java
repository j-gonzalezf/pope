package es.udc.fi.dc.tfg.model.entities;

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
import java.time.LocalDate;
import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class SensationsTest {

    private Sensations voidSensations;
    private Sensations sensations;
    private Templates template;
    private Templates newTemplate;
    private TrainingCycles trainingCycle;
    private Users trainer;
    private Users client;
    private Users newClient;

    @Before
    public void setUp() {
        voidSensations = new Sensations();
        trainer = new Users("trainer@example.com", "password", "Test Trainer",
                "1234567890", null, null);
        client = new Users("client@example.com", "password", "Test Client",
                "1234567890", null, null, null, null, null, trainer);
        newClient = new Users("newClient@example.com", "password", "Test Client",
                "1234567890", null, null, null, null, null, trainer);
        trainingCycle = new TrainingCycles("cycleName", null,
                LocalDate.of(2000, 1, 1), LocalDate.of(2001, 1, 1), trainer, client);
        template = new Templates("templateName",
                LocalDateTime.of(2001, 1, 1, 0, 0), trainingCycle);
        newTemplate = new Templates("templateName",
                LocalDateTime.of(2001, 1, 1, 0, 0), trainingCycle);
        sensations = new Sensations(2, 3, 4, 5,
                LocalDateTime.of(2001, 1, 1, 0, 0), template, client);
    }

    @Test
    public void testSensationsConstructors() {

        // Test void sensations constructor
        assertNotNull(voidSensations);
        assertNull(voidSensations.getId());

        // Test sensations constructor
        assertNotNull(sensations);
        assertEquals(template, sensations.getTemplate());
        assertEquals(client, sensations.getClient());

    }

    @Test
    public void testSensationsGettersAndSetters() {

        // Test setters
        sensations.setId(3L);
        sensations.setFatigue(5);
        sensations.setStiffness(4);
        sensations.setMotivation(3);
        sensations.setSleep(2);
        sensations.setSensationDate(LocalDateTime.of(2002, 1, 1, 0, 0));
        sensations.setTemplate(newTemplate);
        sensations.setClient(newClient);

        // Test getters
        assertEquals(3L, sensations.getId().longValue());
        assertEquals(5, sensations.getFatigue().intValue());
        assertEquals(4, sensations.getStiffness().intValue());
        assertEquals(3, sensations.getMotivation().intValue());
        assertEquals(2, sensations.getSleep().intValue());
        assertEquals(LocalDateTime.of(2002, 1, 1, 0, 0), sensations.getSensationDate());
        assertEquals(newTemplate, sensations.getTemplate());
        assertEquals(newClient, sensations.getClient());

    }

}
