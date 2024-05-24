package es.udc.fi.dc.tfg.model.services;

import es.udc.fi.dc.tfg.model.common.exceptions.DuplicateInstanceException;
import es.udc.fi.dc.tfg.model.common.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.tfg.model.entities.TrainingCycles;
import es.udc.fi.dc.tfg.model.entities.Users;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Clase TrainingCycleServiceTest.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TrainingCycleServiceTest {

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

}
