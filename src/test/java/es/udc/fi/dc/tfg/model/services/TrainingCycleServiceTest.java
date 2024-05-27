package es.udc.fi.dc.tfg.model.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import es.udc.fi.dc.tfg.model.common.exceptions.DuplicateInstanceException;
import es.udc.fi.dc.tfg.model.common.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.tfg.model.entities.TrainingCycles;
import es.udc.fi.dc.tfg.model.entities.Users;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Clase TrainingCycleServiceTest.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TrainingCycleServiceTest {

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
     * Crea un cliente.
     *
     * @param email El email del cliente.
     * @return El objeto Users que representa al cliente con el perfil
     * actualizado.
     */
    private TrainingCycles createCycle() {
        return new TrainingCycles("cycleName", "description", LocalDate.of(2000, 1, 1),
                LocalDate.of(2001, 1, 1), null, null);
    }

    /**
     * Test para crear ciclos de entrenamiento.
     */
    @Test
    public void testCreateCycle() {

        TrainingCycles cycle = createCycle();
        Users trainer = new Users("t@t.com", "password1", "fullName1", "987654321", "", "");
        Users client = new Users("c@c.com", "password2", "fullName2", "123456789", "",
                LocalDate.of(2000, 1, 1), "No", "Ninguno", new BigDecimal("170"), trainer);

        cycle.setTrainer(trainer);
        cycle.setClient(client);

        cycleService.createCycle(cycle);

    }

    /**
     * Test para obtener los ciclos del cliente de un entrenador.
     *
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email.
     * @throws InstanceNotFoundException si no se encuentra ningún ciclo.
     */
    @Test
    public void testGetCycles() throws DuplicateInstanceException, InstanceNotFoundException {

        TrainingCycles cycle1 = createCycle();
        TrainingCycles cycle2 = createCycle();
        Users trainer = new Users("t@t.com", "password1", "fullName1", "987654321", "", "");
        Users client = new Users("c@c.com", "password2", "fullName2", "123456789", "",
                LocalDate.of(2000, 1, 1), "No", "Ninguno", new BigDecimal("170"), trainer);

        userService.signUp(trainer);
        userService.signUp(client);

        cycle1.setTrainer(trainer);
        cycle1.setClient(client);
        cycleService.createCycle(cycle1);

        cycle2.setTrainer(trainer);
        cycle2.setClient(client);
        cycleService.createCycle(cycle2);

        List<TrainingCycles> cycles = cycleService.getCycles(trainer.getId(), client.getId());

        assertEquals(2, cycles.size());
        assertTrue(cycles.contains(cycle1));
        assertTrue(cycles.contains(cycle2));

    }

}
