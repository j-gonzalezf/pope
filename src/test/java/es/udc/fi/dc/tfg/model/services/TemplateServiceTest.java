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
import es.udc.fi.dc.tfg.model.common.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.tfg.model.entities.Exercises;
import es.udc.fi.dc.tfg.model.entities.TemplateRows;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Clase TemplateServiceTest.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TemplateServiceTest {

    private final Long NON_EXISTENT_ID = Long.valueOf(-1);

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
     * Test para crear plantillas de entrenamiento y obtenerlas a partir del ID.
     *
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email.
     * @throws InstanceNotFoundException si no se encuentra ninguna plantilla.
     */
    @Test
    public void testCreateTemplateAndGetTemplateInfo()
            throws DuplicateInstanceException, InstanceNotFoundException {

        Templates template = createTemplate();
        Users trainer = new Users("t@t.com", "password1", "fullName1", "987654321", "", "");
        Users client = new Users("c@c.com", "password2", "fullName2", "123456789", "",
                LocalDate.of(2000, 1, 1), "No", "Ninguno", new BigDecimal("170"), trainer);

        userService.signUp(trainer);
        userService.signUp(client);

        TrainingCycles cycle = new TrainingCycles("cycleName", "description", LocalDate.of(2000, 1, 1),
                LocalDate.of(2001, 1, 1), trainer, client);

        cycleService.createCycle(cycle);

        template.setCycle(cycle);

        templateService.createTemplate(template);

        Templates getTemplate = templateService.getTemplateInfo(template.getId());

        assertEquals(template.getId(), getTemplate.getId());
        assertEquals(template.getName(), getTemplate.getName());
        assertEquals(template.getCreationDate(), getTemplate.getCreationDate());
        assertEquals(template.getCycle(), getTemplate.getCycle());

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
        cycleService.createCycle(cycle);

        Exercises exercise = new Exercises("exerciseName", "description", "exerciseType",
                null, null, null, trainer);
        exerciseService.addExercise(exercise);

        Templates template = createTemplate();
        template.setCycle(cycle);
        templateService.createTemplate(template);

        templateRow.setTemplate(template);
        templateRow.setExercise(exercise);
        templateService.addTemplateRow(templateRow);

    }

    /**
     * Test para obtener las plantillas de un ciclo.
     *
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email.
     */
    @Test
    public void testGetTemplates() throws DuplicateInstanceException {

        Templates template1 = createTemplate();
        Templates template2 = createTemplate();
        Users trainer = new Users("t@t.com", "password1", "fullName1", "987654321", "", "");
        Users client = new Users("c@c.com", "password2", "fullName2", "123456789", "",
                LocalDate.of(2000, 1, 1), "No", "Ninguno", new BigDecimal("170"), trainer);

        userService.signUp(trainer);
        userService.signUp(client);

        TrainingCycles cycle = new TrainingCycles("cycleName", "description", LocalDate.of(2000, 1, 1),
                LocalDate.of(2001, 1, 1), trainer, client);

        cycleService.createCycle(cycle);

        template1.setCycle(cycle);
        template2.setCycle(cycle);

        templateService.createTemplate(template1);
        templateService.createTemplate(template2);

        List<Templates> templates = templateService.getTemplates(cycle.getId());

        assertEquals(2, templates.size());
        assertTrue(templates.contains(template1));
        assertTrue(templates.contains(template2));

    }

    /**
     * Test para obtener las filas de una plantilla.
     *
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email.
     */
    @Test
    public void testGetTemplateRows() throws DuplicateInstanceException {

        TemplateRows templateRow1 = addTemplateRow();
        TemplateRows templateRow2 = addTemplateRow();

        Users trainer = new Users("t@t.com", "password1", "fullName1", "987654321", "", "");
        Users client = new Users("c@c.com", "password2", "fullName2", "123456789", "",
                LocalDate.of(2000, 1, 1), "No", "Ninguno", new BigDecimal("170"), trainer);
        userService.signUp(trainer);
        userService.signUp(client);

        TrainingCycles cycle = new TrainingCycles("cycleName", "description", LocalDate.of(2000, 1, 1),
                LocalDate.of(2001, 1, 1), trainer, client);
        cycleService.createCycle(cycle);

        Exercises exercise = new Exercises("exerciseName", "description", "exerciseType",
                null, null, null, trainer);
        exerciseService.addExercise(exercise);

        Templates template = createTemplate();
        template.setCycle(cycle);
        templateService.createTemplate(template);

        templateRow1.setTemplate(template);
        templateRow1.setExercise(exercise);
        templateService.addTemplateRow(templateRow1);
        templateRow2.setTemplate(template);
        templateRow2.setExercise(exercise);
        templateService.addTemplateRow(templateRow2);

        List<TemplateRows> templateRows = templateService.getTemplateRows(template.getId());

        assertEquals(2, templateRows.size());
        assertTrue(templateRows.contains(templateRow1));
        assertTrue(templateRows.contains(templateRow2));

    }

    /**
     * Test para obtener una plantilla con un ID inexistente.
     *
     * @throws InstanceNotFoundException si no se encuentra una plantilla con el
     * ID proporcionado.
     */
    @Test
    public void testGetTemplateWithNonExistentId() throws InstanceNotFoundException {
        assertThrows(InstanceNotFoundException.class,
                () -> templateService.getTemplateInfo(NON_EXISTENT_ID));
    }

    /**
     * Test para editar plantillas.
     *
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email.
     * @throws InstanceNotFoundException si no se encuentra ninguna plantilla.
     */
    @Test
    public void testUpdateTemplate()
            throws DuplicateInstanceException, InstanceNotFoundException {

        Templates template = createTemplate();
        Users trainer = new Users("t@t.com", "password1", "fullName1", "987654321", "", "");
        Users client = new Users("c@c.com", "password2", "fullName2", "123456789", "",
                LocalDate.of(2000, 1, 1), "No", "Ninguno", new BigDecimal("170"), trainer);

        userService.signUp(trainer);
        userService.signUp(client);

        TrainingCycles cycle = new TrainingCycles("cycleName", "description", LocalDate.of(2000, 1, 1),
                LocalDate.of(2001, 1, 1), trainer, client);

        cycleService.createCycle(cycle);

        template.setCycle(cycle);

        templateService.createTemplate(template);

        Templates updatedTemplate = templateService.updateTemplate(template.getId(), "new Name");

        assertEquals("new Name", updatedTemplate.getName());

    }

    /**
     * Test para actualizar una plantilla con un ID inexistente.
     *
     * @throws InstanceNotFoundException si no se encuentra una plantilla con el
     * ID proporcionado.
     */
    @Test
    public void testUpdateTemplateWithNonExistentId() throws InstanceNotFoundException {
        assertThrows(InstanceNotFoundException.class,
                () -> templateService.updateTemplate(NON_EXISTENT_ID, "new Name"));
    }

    /**
     * Test para editar filas de una plantilla.
     *
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email.
     * @throws InstanceNotFoundException si no se encuentra ninguna fila.
     */
    @Test
    public void testUpdateTemplateRow()
            throws DuplicateInstanceException, InstanceNotFoundException {

        TemplateRows templateRow = addTemplateRow();

        Users trainer = new Users("t@t.com", "password1", "fullName1", "987654321", "", "");
        Users client = new Users("c@c.com", "password2", "fullName2", "123456789", "",
                LocalDate.of(2000, 1, 1), "No", "Ninguno", new BigDecimal("170"), trainer);
        userService.signUp(trainer);
        userService.signUp(client);

        TrainingCycles cycle = new TrainingCycles("cycleName", "description", LocalDate.of(2000, 1, 1),
                LocalDate.of(2001, 1, 1), trainer, client);
        cycleService.createCycle(cycle);

        Exercises exercise1 = new Exercises("exerciseName", "description", "exerciseType",
                null, null, null, trainer);
        exerciseService.addExercise(exercise1);

        Exercises exercise2 = new Exercises("exerciseName2", "description", "exerciseType",
                null, null, null, trainer);
        exerciseService.addExercise(exercise2);

        Templates template = createTemplate();
        template.setCycle(cycle);
        templateService.createTemplate(template);

        templateRow.setTemplate(template);
        templateRow.setExercise(exercise1);
        templateService.addTemplateRow(templateRow);

        TemplateRows updatedRow = templateService.updateTemplateRow(templateRow.getId(),
                2, 3, new BigDecimal("40"), exercise2);

        assertEquals("2", updatedRow.getSeries().toString());
        assertEquals("3", updatedRow.getRepetitions().toString());
        assertEquals(new BigDecimal("40"), updatedRow.getWeight());
        assertEquals(exercise2, updatedRow.getExercise());

    }

    /**
     * Test para actualizar una fila con un ID inexistente.
     *
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email.
     * @throws InstanceNotFoundException si no se encuentra una fila con el ID
     * proporcionado.
     */
    @Test
    public void testUpdateTemplateRowWithNonExistentId()
            throws DuplicateInstanceException, InstanceNotFoundException {

        Users trainer = new Users("t@t.com", "password1", "fullName1", "987654321", "", "");

        userService.signUp(trainer);

        Exercises exercise1 = new Exercises("exerciseName", "description", "exerciseType",
                null, null, null, trainer);
        exerciseService.addExercise(exercise1);

        assertThrows(InstanceNotFoundException.class,
                () -> templateService.updateTemplateRow(NON_EXISTENT_ID, 2, 3,
                        new BigDecimal("40"), exercise1));

    }

    /**
     * Test para eliminar una plantilla.
     *
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email.
     * @throws InstanceNotFoundException si no se encuentra una plantilla con el
     * ID proporcionado.
     */
    @Test
    public void testDeleteTemplate()
            throws DuplicateInstanceException, InstanceNotFoundException {

        Templates template = createTemplate();
        Users trainer = new Users("t@t.com", "password1", "fullName1", "987654321", "", "");
        Users client = new Users("c@c.com", "password2", "fullName2", "123456789", "",
                LocalDate.of(2000, 1, 1), "No", "Ninguno", new BigDecimal("170"), trainer);

        userService.signUp(trainer);
        userService.signUp(client);

        TrainingCycles cycle = new TrainingCycles("cycleName", "description", LocalDate.of(2000, 1, 1),
                LocalDate.of(2001, 1, 1), trainer, client);

        cycleService.createCycle(cycle);

        template.setCycle(cycle);

        templateService.createTemplate(template);

        Long deletedTemplateId = templateService.deleteTemplate(template.getId());

        // Comprobar que el ID de la plantilla eliminada es el correcto
        assertEquals(template.getId(), deletedTemplateId);

    }

    /**
     * Test para eliminar una plantilla con un ID inexistente.
     *
     * @throws InstanceNotFoundException si no se encuentra una plantilla con el
     * ID proporcionado.
     */
    @Test
    public void testDeleteTemplateWithNonExistentId() throws InstanceNotFoundException {
        assertThrows(InstanceNotFoundException.class,
                () -> templateService.deleteTemplate(NON_EXISTENT_ID));
    }

    /**
     * Test para eliminar la fila de una plantilla.
     *
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email.
     * @throws InstanceNotFoundException si no se encuentra una fila con el ID
     * proporcionado.
     */
    @Test
    public void testDeleteTemplateRow()
            throws DuplicateInstanceException, InstanceNotFoundException {

        TemplateRows templateRow = addTemplateRow();

        Users trainer = new Users("t@t.com", "password1", "fullName1", "987654321", "", "");
        Users client = new Users("c@c.com", "password2", "fullName2", "123456789", "",
                LocalDate.of(2000, 1, 1), "No", "Ninguno", new BigDecimal("170"), trainer);
        userService.signUp(trainer);
        userService.signUp(client);

        TrainingCycles cycle = new TrainingCycles("cycleName", "description", LocalDate.of(2000, 1, 1),
                LocalDate.of(2001, 1, 1), trainer, client);
        cycleService.createCycle(cycle);

        Exercises exercise = new Exercises("exerciseName", "description", "exerciseType",
                null, null, null, trainer);
        exerciseService.addExercise(exercise);

        Templates template = createTemplate();
        template.setCycle(cycle);
        templateService.createTemplate(template);

        templateRow.setTemplate(template);
        templateRow.setExercise(exercise);
        templateService.addTemplateRow(templateRow);

        Long deletedRowId = templateService.deleteTemplateRow(templateRow.getId());

        // Comprobar que el ID de la fila eliminada es el correcto
        assertEquals(templateRow.getId(), deletedRowId);

    }

    /**
     * Test para eliminar la fila de una plantilla con un ID inexistente.
     *
     * @throws InstanceNotFoundException si no se encuentra una fila con el ID
     * proporcionado.
     */
    @Test
    public void testDeleteTemplateRowWithNonExistentId() throws InstanceNotFoundException {
        assertThrows(InstanceNotFoundException.class,
                () -> templateService.deleteTemplateRow(NON_EXISTENT_ID));
    }

}
