package es.udc.fi.dc.tfg.model.services;

import es.udc.fi.dc.tfg.model.common.exceptions.DuplicateInstanceException;
import es.udc.fi.dc.tfg.model.common.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.tfg.model.entities.Sensations;
import es.udc.fi.dc.tfg.model.entities.Templates;
import es.udc.fi.dc.tfg.model.entities.TrainingCycles;
import es.udc.fi.dc.tfg.model.entities.Users;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Clase SensationServiceTest.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class SensationServiceTest {

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
     * El template service.
     */
    @Autowired
    private TemplateService templateService;

    /**
     * El sensation service.
     */
    @Autowired
    private SensationService sensationService;

    /**
     * Crea un registro de sensaciones.
     *
     * @return El objeto Sensations que representa el registro de sensaciones
     */
    private Sensations sensationsRegister() {
        return new Sensations(2, 3, 4, 5, LocalDateTime.of(2001, 1, 1, 0, 0), null, null);
    }

    /**
     * Test para crear registros de sensaciones.
     *
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email.
     * @throws InstanceNotFoundException si no se encuentra ninguna plantilla.
     * @throws IOException si hay algún error a la hora de guardar la imagen.
     */
    @Test
    public void testSensationsRegisterAndGetSensationInfo()
            throws DuplicateInstanceException, InstanceNotFoundException, IOException {

        Sensations sensations = sensationsRegister();
        Users trainer = new Users("t@t.com", "password1", "fullName1", "987654321", "", "");
        Users client = new Users("c@c.com", "password2", "fullName2", "123456789", "",
                LocalDate.of(2000, 1, 1), "No", "Ninguno", new BigDecimal("170"), trainer);

        userService.signUp(trainer, null);
        userService.signUp(client, null);

        TrainingCycles cycle = new TrainingCycles("cycleName", "description",
                LocalDate.of(2000, 1, 1), LocalDate.of(2001, 1, 1), trainer, client);

        cycleService.createCycle(cycle);

        Templates template = new Templates("templateName",
                LocalDateTime.of(2000, 1, 1, 0, 0), cycle);

        templateService.createTemplate(template);

        sensations.setTemplate(template);
        sensations.setClient(client);

        sensationService.sensationsRegister(sensations);

        Sensations getSensations = sensationService.getSensationInfo(template.getId());

        assertEquals(sensations.getId(), getSensations.getId());
        assertEquals(sensations.getFatigue(), getSensations.getFatigue());
        assertEquals(sensations.getStiffness(), getSensations.getStiffness());
        assertEquals(sensations.getMotivation(), getSensations.getMotivation());
        assertEquals(sensations.getSleep(), getSensations.getSleep());
        assertEquals(sensations.getSensationDate(), getSensations.getSensationDate());
        assertEquals(sensations.getTemplate(), getSensations.getTemplate());
        assertEquals(sensations.getClient(), getSensations.getClient());

    }

    /**
     * Test para obtener los registros de sensaciones de un cliente.
     *
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email.
     * @throws IOException si hay algún error a la hora de guardar la imagen.
     */
    @Test
    public void testGetSensations() throws DuplicateInstanceException, IOException {

        Sensations sensations1 = sensationsRegister();
        Sensations sensations2 = sensationsRegister();
        Users trainer = new Users("t@t.com", "password1", "fullName1", "987654321", "", "");
        Users client = new Users("c@c.com", "password2", "fullName2", "123456789", "",
                LocalDate.of(2000, 1, 1), "No", "Ninguno", new BigDecimal("170"), trainer);

        userService.signUp(trainer, null);
        userService.signUp(client, null);

        TrainingCycles cycle = new TrainingCycles("cycleName", "description",
                LocalDate.of(2000, 1, 1), LocalDate.of(2001, 1, 1), trainer, client);

        cycleService.createCycle(cycle);

        Templates template1 = new Templates("templateName1",
                LocalDateTime.of(2000, 1, 1, 0, 0), cycle);
        Templates template2 = new Templates("templateName2",
                LocalDateTime.of(2000, 1, 1, 0, 0), cycle);

        templateService.createTemplate(template1);
        templateService.createTemplate(template2);

        sensations1.setTemplate(template1);
        sensations1.setClient(client);

        sensations2.setTemplate(template2);
        sensations2.setClient(client);

        sensationService.sensationsRegister(sensations1);
        sensationService.sensationsRegister(sensations2);

        List<Sensations> sensations = sensationService.getSensations(client.getId());

        assertEquals(2, sensations.size());
        assertTrue(sensations.contains(sensations1));
        assertTrue(sensations.contains(sensations2));

    }

    /**
     * Test para editar el registro de sensaciones.
     *
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email.
     * @throws InstanceNotFoundException si no se encuentra ningún registro.
     * @throws IOException si hay algún error a la hora de guardar la imagen.
     */
    @Test
    public void testUpdateSensations()
            throws DuplicateInstanceException, InstanceNotFoundException, IOException {

        Sensations sensations = sensationsRegister();
        Users trainer = new Users("t@t.com", "password1", "fullName1", "987654321", "", "");
        Users client = new Users("c@c.com", "password2", "fullName2", "123456789", "",
                LocalDate.of(2000, 1, 1), "No", "Ninguno", new BigDecimal("170"), trainer);

        userService.signUp(trainer, null);
        userService.signUp(client, null);

        TrainingCycles cycle = new TrainingCycles("cycleName", "description",
                LocalDate.of(2000, 1, 1), LocalDate.of(2001, 1, 1), trainer, client);

        cycleService.createCycle(cycle);

        Templates template = new Templates("templateName",
                LocalDateTime.of(2000, 1, 1, 0, 0), cycle);

        templateService.createTemplate(template);

        sensations.setTemplate(template);
        sensations.setClient(client);

        sensationService.sensationsRegister(sensations);

        Sensations updatedSensations = sensationService.updateSensations(
                sensations.getId(), 5, 4, 3, 2);

        assertEquals(5, updatedSensations.getFatigue().longValue());
        assertEquals(4, updatedSensations.getStiffness().longValue());
        assertEquals(3, updatedSensations.getMotivation().longValue());
        assertEquals(2, updatedSensations.getSleep().longValue());

    }

    /**
     * Test para actualizar un registro con un ID inexistente.
     *
     * @throws InstanceNotFoundException si no se encuentra un registro con el
     * ID proporcionado.
     */
    @Test
    public void testUpdateTemplateWithNonExistentId() throws InstanceNotFoundException {
        assertThrows(InstanceNotFoundException.class,
                () -> sensationService.updateSensations(NON_EXISTENT_ID, 5, 4, 3, 2));
    }

}
