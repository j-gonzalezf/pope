package es.udc.fi.dc.tfg.model.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
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
     * Crea un ciclo de entrenamiento.
     *
     * @return El objeto TrainingCycles que representa al ciclo
     */
    private TrainingCycles createCycle() {
        return new TrainingCycles("cycleName", "description", LocalDate.of(2000, 1, 1),
                LocalDate.of(2001, 1, 1), null, null);
    }

    /**
     * Test para crear ciclos de entrenamiento y obtenerlo a partir de su ID.
     *
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email.
     * @throws InstanceNotFoundException si no se encuentra ningún ciclo.
     */
    @Test
    public void testCreateCycleAndGetCycleInfo()
            throws DuplicateInstanceException, InstanceNotFoundException {

        TrainingCycles cycle = createCycle();
        Users trainer = new Users("t@t.com", "password1", "fullName1", "987654321", "", "");
        Users client = new Users("c@c.com", "password2", "fullName2", "123456789", "",
                LocalDate.of(2000, 1, 1), "No", "Ninguno", new BigDecimal("170"), trainer);

        userService.signUp(trainer);
        userService.signUp(client);

        cycle.setTrainer(trainer);
        cycle.setClient(client);

        cycleService.createCycle(cycle);

        TrainingCycles getCycle = cycleService.getCycleInfo(cycle.getId());

        assertEquals(cycle.getId(), getCycle.getId());
        assertEquals(cycle.getName(), getCycle.getName());
        assertEquals(cycle.getDescription(), getCycle.getDescription());
        assertEquals(cycle.getFromDate(), getCycle.getFromDate());
        assertEquals(cycle.getToDate(), getCycle.getToDate());
        assertEquals(cycle.getTrainer(), getCycle.getTrainer());
        assertEquals(cycle.getClient(), getCycle.getClient());

    }

    /**
     * Test para obtener los ciclos del cliente de un entrenador.
     *
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email.
     */
    @Test
    public void testGetCycles() throws DuplicateInstanceException {

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

    /**
     * Test para obtener un ciclo con un ID inexistente.
     *
     * @throws InstanceNotFoundException si no se encuentra un ciclo con el ID
     * proporcionado.
     */
    @Test
    public void testGetCycleWithNonExistentId() throws InstanceNotFoundException {
        assertThrows(InstanceNotFoundException.class,
                () -> cycleService.getCycleInfo(NON_EXISTENT_ID));
    }

    /**
     * Test para editar ciclos de entrenamiento.
     *
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email.
     * @throws InstanceNotFoundException si no se encuentra ningún ciclo.
     */
    @Test
    public void testUpdateCycle()
            throws DuplicateInstanceException, InstanceNotFoundException {

        TrainingCycles cycle = createCycle();
        Users trainer = new Users("t@t.com", "password1", "fullName1", "987654321", "", "");
        Users client = new Users("c@c.com", "password2", "fullName2", "123456789", "",
                LocalDate.of(2000, 1, 1), "No", "Ninguno", new BigDecimal("170"), trainer);

        userService.signUp(trainer);
        userService.signUp(client);

        cycle.setTrainer(trainer);
        cycle.setClient(client);
        cycleService.createCycle(cycle);

        TrainingCycles updatedCycle = cycleService.updateCycle(cycle.getId(),
                "new Name", "new Description", LocalDate.of(2001, 1, 1),
                LocalDate.of(2002, 1, 1));

        assertEquals("new Name", updatedCycle.getName());
        assertEquals("new Description", updatedCycle.getDescription());
        assertEquals(LocalDate.of(2001, 1, 1), updatedCycle.getFromDate());
        assertEquals(LocalDate.of(2002, 1, 1), updatedCycle.getToDate());

    }

    /**
     * Test para actualizar un ciclo con un ID inexistente.
     *
     * @throws InstanceNotFoundException si no se encuentra un ciclo con el ID
     * proporcionado.
     */
    @Test
    public void testUpdateCycleWithNonExistentId() throws InstanceNotFoundException {
        assertThrows(InstanceNotFoundException.class,
                () -> cycleService.updateCycle(NON_EXISTENT_ID, "new Name",
                        "new Description", LocalDate.of(2001, 1, 1),
                        LocalDate.of(2002, 1, 1)));
    }

    /**
     * Test para eliminar un ciclo.
     *
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email.
     * @throws InstanceNotFoundException si no se encuentra un ciclo con el ID
     * proporcionado.
     */
    @Test
    public void testDeleteCycle()
            throws DuplicateInstanceException, InstanceNotFoundException {

        TrainingCycles cycle = createCycle();
        Users trainer = new Users("t@t.com", "password1", "fullName1", "987654321", "", "");
        Users client = new Users("c@c.com", "password2", "fullName2", "123456789", "",
                LocalDate.of(2000, 1, 1), "No", "Ninguno", new BigDecimal("170"), trainer);

        userService.signUp(trainer);
        userService.signUp(client);

        cycle.setTrainer(trainer);
        cycle.setClient(client);

        cycleService.createCycle(cycle);

        Long deletedCycleId = cycleService.deleteCycle(cycle.getId());

        // Comprobar que el ID del ciclo eliminado es el correcto
        assertEquals(cycle.getId(), deletedCycleId);

    }

    /**
     * Test para eliminar un ciclo con un ID inexistente.
     *
     * @throws InstanceNotFoundException si no se encuentra un ciclo con el ID
     * proporcionado.
     */
    @Test
    public void testDeleteCycleWithNonExistentId() throws InstanceNotFoundException {
        assertThrows(InstanceNotFoundException.class,
                () -> cycleService.deleteCycle(NON_EXISTENT_ID));
    }

}
