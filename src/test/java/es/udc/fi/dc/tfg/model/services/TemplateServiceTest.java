package es.udc.fi.dc.tfg.model.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.tfg.model.entities.Templates;
import es.udc.fi.dc.tfg.model.entities.TrainingCycles;
import es.udc.fi.dc.tfg.model.entities.Users;
import es.udc.fi.dc.tfg.model.common.exceptions.DuplicateInstanceException;
import es.udc.fi.dc.tfg.model.entities.Exercises;
import es.udc.fi.dc.tfg.model.entities.TemplateRows;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Clase TemplateServiceTest.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TemplateServiceTest {

    /**
     * El user service.
     */
    @Autowired
    private UserService userService;

    /**
     * El training cycle service.
     */
    @Autowired
    private TrainingCycleService cycleService;

    /**
     * El exercise service.
     */
    @Autowired
    private ExerciseService exerciseService;

    /**
     * El template service.
     */
    @Autowired
    private TemplateService templateService;

    /**
     * Crea una plantilla de entrenamiento.
     *
     * @return El objeto Templates que representa a la plantilla
     */
    private Templates createTemplate() {
        return new Templates("templateName", LocalDateTime.of(2000, 1, 1, 0, 0),
                null);
    }

    /**
     * Añade una fila para la plantilla de entrenamiento.
     *
     * @return El objeto TemplateRows que representa la fila de la plantilla
     */
    private TemplateRows addTemplateRow() {
        return new TemplateRows(3, 5, new BigDecimal("50"), null, null);
    }

    /**
     * Test para crear plantillas de entrenamiento.
     *
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email.
     */
    @Test
    public void testCreateTemplate() throws DuplicateInstanceException {

        Templates template = createTemplate();
        Users trainer = new Users("t@t.com", "password1", "fullName1", "987654321", "", "");
        Users client = new Users("c@c.com", "password2", "fullName2", "123456789", "",
                LocalDate.of(2000, 1, 1), "No", "Ninguno", new BigDecimal("170"), trainer);

        userService.signUp(trainer);
        userService.signUp(client);

        TrainingCycles cycle = new TrainingCycles("cycleName", "description", LocalDate.of(2000, 1, 1),
                LocalDate.of(2001, 1, 1), trainer, client);

        cycle.setTrainer(trainer);
        cycle.setClient(client);

        cycleService.createCycle(cycle);

        template.setCycle(cycle);

        templateService.createTemplate(template);

    }

    /**
     * Test para crear filas de plantillas de entrenamiento.
     *
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email.
     */
    @Test
    public void testAddTemplateRow() throws DuplicateInstanceException {

        TemplateRows templateRow = addTemplateRow();

        Users trainer = new Users("t@t.com", "password1", "fullName1", "987654321", "", "");
        Users client = new Users("c@c.com", "password2", "fullName2", "123456789", "",
                LocalDate.of(2000, 1, 1), "No", "Ninguno", new BigDecimal("170"), trainer);
        userService.signUp(trainer);
        userService.signUp(client);

        TrainingCycles cycle = new TrainingCycles("cycleName", "description", LocalDate.of(2000, 1, 1),
                LocalDate.of(2001, 1, 1), trainer, client);
        cycle.setTrainer(trainer);
        cycle.setClient(client);
        cycleService.createCycle(cycle);

        Exercises exercise = new Exercises("exerciseName", "description", "exerciseType",
                null, null, null, null);
        exercise.setTrainer(trainer);
        exerciseService.addExercise(exercise);

        Templates template = createTemplate();
        template.setCycle(cycle);
        templateService.createTemplate(template);

        templateRow.setTemplate(template);
        templateRow.setExercise(exercise);
        templateService.addTemplateRow(templateRow);

    }

}
