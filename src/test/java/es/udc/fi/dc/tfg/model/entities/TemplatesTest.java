package es.udc.fi.dc.tfg.model.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
public class TemplatesTest {

    private TemplateRows voidTemplateRow;
    private TemplateRows templateRow;
    private Templates voidTemplate;
    private Templates template;
    private Templates newTemplate;
    private Exercises exercise;
    private Exercises newExercise;
    private TrainingCycles trainingCycle;
    private TrainingCycles newTrainingCycle;
    private Users trainer;
    private Users client;

    @Before
    public void setUp() {
        voidTemplate = new Templates();
        voidTemplateRow = new TemplateRows();
        trainer = new Users("trainer@example.com", "password", "Test Trainer",
                "1234567890", null, null);
        client = new Users("client@example.com", "password", "Test Client",
                "1234567890", null, null, null, null, null, trainer);
        trainingCycle = new TrainingCycles("cycleName", null,
                LocalDate.of(2000, 1, 1), LocalDate.of(2001, 1, 1), trainer, client);
        newTrainingCycle = new TrainingCycles("cycleName", null,
                LocalDate.of(2000, 1, 1), LocalDate.of(2001, 1, 1), trainer, client);
        exercise = new Exercises("exerciseName", "exerciseDescription",
                "exerciseType", null, null, null, trainer);
        newExercise = new Exercises("exerciseName", "exerciseDescription",
                "exerciseType", null, null, null, trainer);
        template = new Templates("templateName",
                LocalDateTime.of(2001, 1, 1, 0, 0), trainingCycle);
        newTemplate = new Templates("templateName",
                LocalDateTime.of(2001, 1, 1, 0, 0), trainingCycle);
        templateRow = new TemplateRows(3, 10, new BigDecimal("30"), exercise, template);
    }

    @Test
    public void testTemplateConstructors() {

        // Test template constructor
        assertNotNull(voidTemplate);
        assertNull(voidTemplate.getId());

        // Test template constructor
        assertNotNull(template);
        assertEquals("templateName", template.getName());
        assertEquals(LocalDateTime.of(2001, 1, 1, 0, 0), template.getCreationDate());
        assertEquals(trainingCycle, template.getCycle());

    }

    @Test
    public void testTemplateGettersAndSetters() {

        // Test setters
        template.setId(3L);
        template.setName("newTemplateName");
        template.setCreationDate(LocalDateTime.of(2000, 2, 1, 0, 0));
        template.setCycle(newTrainingCycle);

        // Test getters
        assertEquals(3L, template.getId().longValue());
        assertEquals("newTemplateName", template.getName());
        assertEquals(LocalDateTime.of(2000, 2, 1, 0, 0), template.getCreationDate());
        assertEquals(newTrainingCycle, template.getCycle());

    }

    @Test
    public void testTemplateRowConstructors() {

        // Test template row constructor
        assertNotNull(voidTemplateRow);
        assertNull(voidTemplateRow.getId());

        // Test template row constructor
        assertNotNull(templateRow);
        assertEquals(exercise, templateRow.getExercise());
        assertEquals(template, templateRow.getTemplate());

    }

    @Test
    public void testTemplateRowGettersAndSetters() {

        // Test setters
        templateRow.setId(3L);
        templateRow.setSeries(5);
        templateRow.setRepetitions(6);
        templateRow.setWeight(new BigDecimal("50"));
        templateRow.setExercise(newExercise);
        templateRow.setTemplate(newTemplate);

        // Test getters
        assertEquals(3L, templateRow.getId().longValue());
        assertEquals(5, templateRow.getSeries().intValue());
        assertEquals(6, templateRow.getRepetitions().intValue());
        assertEquals(new BigDecimal("50"), templateRow.getWeight());
        assertEquals(newExercise, templateRow.getExercise());
        assertEquals(newTemplate, templateRow.getTemplate());

    }

}
