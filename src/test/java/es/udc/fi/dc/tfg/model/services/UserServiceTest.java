package es.udc.fi.dc.tfg.model.services;

import jakarta.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import es.udc.fi.dc.tfg.model.common.exceptions.DuplicateInstanceException;
import es.udc.fi.dc.tfg.model.entities.Users;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.Assert.assertThrows;

/**
 * Clase UserServiceTest.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserServiceTest {

    private final Long NON_EXISTENT_ID = Long.valueOf(-1);

    /**
     * El trainer service.
     */
    @Autowired
    private UserService userService;

    /**
     * Crea un entrenador.
     *
     * @param email El email del entrenador.
     * @return El objeto Users que representa al entrenador con el perfil
     * actualizado.
     */
    private Users createTrainer(String email) {
        return new Users(email, "password1", "fullName1", "987654321", "", "");
    }

    /**
     * Crea un cliente.
     *
     * @param email El email del cliente.
     * @return El objeto Users que representa al cliente con el perfil
     * actualizado.
     */
    private Users createClient(String email) {
        return new Users(email, "password2", "fullName2", "123456789", "",
                LocalDateTime.of(2000, 1, 1, 0, 0), "No", "Ninguno", new BigDecimal("170"));
    }

    @Test
    public void testSignUpDuplicatedEmail() throws DuplicateInstanceException {

        Users trainer = createTrainer("trainer@trainer.com");
        Users client = createClient("client@client.com");

        userService.signUp(trainer);
        userService.signUp(client);

        assertThrows(DuplicateInstanceException.class, () -> userService.signUp(trainer));
        assertThrows(DuplicateInstanceException.class, () -> userService.signUp(client));

    }

}
