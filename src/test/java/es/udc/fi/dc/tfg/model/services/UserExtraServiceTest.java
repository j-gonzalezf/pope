package es.udc.fi.dc.tfg.model.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.tfg.model.common.exceptions.DuplicateInstanceException;
import es.udc.fi.dc.tfg.model.common.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.tfg.model.entities.Users;
import es.udc.fi.dc.tfg.model.entities.Weights;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Clase UserExtraServiceTest.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserExtraServiceTest {

    /**
     * El user service.
     */
    @Autowired
    private UserService userService;

    /**
     * El user service.
     */
    @Autowired
    private UserExtraService userExtraService;

    /**
     * Crea un registro de peso para un cliente.
     *
     * @return El objeto Templates que representa a la plantilla
     */
    private Weights createWeightRegister() {
        return new Weights(new BigDecimal("50"),
                LocalDateTime.of(2000, 1, 1, 0, 0), null);
    }

    /**
     * Test para crear un registro de peso y obtener el último.
     *
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email.
     * @throws InstanceNotFoundException si no se encuentra ninguna plantilla.
     * @throws IOException si hay algún error a la hora de guardar la imagen.
     */
    @Test
    public void testWeightRegisterAndGetLastWeight()
            throws DuplicateInstanceException, InstanceNotFoundException, IOException {

        Weights weight = createWeightRegister();
        Weights weight2 = createWeightRegister();
        Users trainer = new Users("t@t.com", "password1", "fullName1", "987654321", "", "");
        Users client = new Users("c@c.com", "password2", "fullName2", "123456789", "",
                LocalDate.of(2000, 1, 1), "No", "Ninguno", new BigDecimal("170"), trainer);

        userService.signUp(trainer, null);
        userService.signUp(client, null);

        weight.setClient(client);
        weight2.setClient(client);

        weight2.setWeightDate(LocalDateTime.of(2001, 1, 1, 0, 0));

        userExtraService.weightRegister(weight);
        userExtraService.weightRegister(weight2);

        Weights getLastWeight = userExtraService.getLastWeight(client.getId());

        assertEquals(weight2.getId(), getLastWeight.getId());
        assertEquals(weight2.getWeight(), getLastWeight.getWeight());
        assertEquals(weight2.getWeightDate(), getLastWeight.getWeightDate());
        assertEquals(weight2.getClient(), getLastWeight.getClient());

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

        Weights weight = createWeightRegister();
        Weights weight2 = createWeightRegister();
        Users trainer = new Users("t@t.com", "password1", "fullName1", "987654321", "", "");
        Users client = new Users("c@c.com", "password2", "fullName2", "123456789", "",
                LocalDate.of(2000, 1, 1), "No", "Ninguno", new BigDecimal("170"), trainer);

        userService.signUp(trainer, null);
        userService.signUp(client, null);

        weight.setClient(client);
        weight2.setClient(client);

        weight2.setWeightDate(LocalDateTime.of(2001, 1, 1, 0, 0));

        userExtraService.weightRegister(weight);
        userExtraService.weightRegister(weight2);

        List<Weights> weights = userExtraService.getWeights(client.getId());

        assertEquals(2, weights.size());
        assertTrue(weights.contains(weight));
        assertTrue(weights.contains(weight2));

    }

}
