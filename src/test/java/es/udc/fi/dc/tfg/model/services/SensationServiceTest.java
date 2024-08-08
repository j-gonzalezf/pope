package es.udc.fi.dc.tfg.model.services;

import es.udc.fi.dc.tfg.model.common.exceptions.DuplicateInstanceException;
import es.udc.fi.dc.tfg.model.common.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.tfg.model.entities.Sensations;
import es.udc.fi.dc.tfg.model.entities.Templates;
import es.udc.fi.dc.tfg.model.entities.TrainingCycles;
import es.udc.fi.dc.tfg.model.entities.Users;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
     */
    @Test
    public void testCreateTemplateAndGetTemplateInfo()
            throws DuplicateInstanceException, InstanceNotFoundException {

        Sensations sensations = sensationsRegister();
        Users trainer = new Users("t@t.com", "password1", "fullName1", "987654321", "", "");
        Users client = new Users("c@c.com", "password2", "fullName2", "123456789", "",
                LocalDate.of(2000, 1, 1), "No", "Ninguno", new BigDecimal("170"), trainer);

        userService.signUp(trainer);
        userService.signUp(client);

        TrainingCycles cycle = new TrainingCycles("cycleName", "description",
                LocalDate.of(2000, 1, 1), LocalDate.of(2001, 1, 1), trainer, client);

        cycleService.createCycle(cycle);

        Templates template = new Templates("templateName",
                LocalDateTime.of(2000, 1, 1, 0, 0), cycle);

        templateService.createTemplate(template);

        sensations.setTemplate(template);
        sensations.setClient(client);

        sensationService.sensationsRegister(sensations);

    }

}
