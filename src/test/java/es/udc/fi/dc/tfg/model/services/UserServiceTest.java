package es.udc.fi.dc.tfg.model.services;

import jakarta.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import es.udc.fi.dc.tfg.model.common.exceptions.DuplicateInstanceException;
import es.udc.fi.dc.tfg.model.common.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.tfg.model.entities.Users;
import es.udc.fi.dc.tfg.model.services.exceptions.IncorrectLoginException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.Assert.assertEquals;
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

    /**
     * Test para crear usuarios e iniciar sesión a partir de su ID.
     *
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email.
     * @throws InstanceNotFoundException si no se encuentra un usuario con el ID
     * proporcionado.
     */
    @Test
    public void testSignUpAndLoginFromId() throws DuplicateInstanceException, InstanceNotFoundException {

        Users trainer = createTrainer("trainer@trainer.com");
        Users client = createClient("client@client.com");

        userService.signUp(trainer);
        userService.signUp(client);

        Users loggedInTrainer = userService.loginFromId(trainer.getId());
        Users loggedInClient = userService.loginFromId(client.getId());

        assertEquals(trainer, loggedInTrainer);
        assertEquals(client, loggedInClient);
        assertEquals(Users.RoleType.TRAINER, trainer.getRole());
        assertEquals(Users.RoleType.CLIENT, client.getRole());

    }

    /**
     * Test para registrarse con un email existente.
     *
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email.
     */
    @Test
    public void testSignUpDuplicatedEmail() throws DuplicateInstanceException {

        Users trainer = createTrainer("trainer@trainer.com");
        Users client = createClient("client@client.com");

        userService.signUp(trainer);
        userService.signUp(client);

        assertThrows(DuplicateInstanceException.class, () -> userService.signUp(trainer));
        assertThrows(DuplicateInstanceException.class, () -> userService.signUp(client));

    }

    /**
     * Test para buscar usuarios e iniciar sesión con un ID inexistente.
     */
    @Test
    public void testLoginFromNonExistentId() {
        assertThrows(InstanceNotFoundException.class, () -> userService.loginFromId(NON_EXISTENT_ID));
    }

    /**
     * Test para iniciar sesión.
     *
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email.
     * @throws IncorrectLoginException si el email o la contraseña son
     * incorrectos.
     */
    @Test
    public void testLogin() throws DuplicateInstanceException, IncorrectLoginException {

        Users trainer = createTrainer("trainer@trainer.com");
        Users client = createClient("client@client.com");

        String clearPassword = trainer.getPassword();
        String clearClientPassword = client.getPassword();

        userService.signUp(trainer);
        userService.signUp(client);

        Users loggedInTrainer = userService.login(trainer.getEmail(), clearPassword);
        Users loggedInClient = userService.login(client.getEmail(), clearClientPassword);

        assertEquals(trainer, loggedInTrainer);
        assertEquals(client, loggedInClient);

    }

    /**
     * Test para iniciar sesión con un email inexistente.
     */
    @Test
    public void testLoginWithNonExistentEmail() {
        assertThrows(IncorrectLoginException.class, () -> userService.login("X", "Y"));
    }

    /**
     * Test para iniciar sesión con una contraseña errónea.
     *
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email.
     */
    @Test
    public void testLoginWithIncorrectPassword() throws DuplicateInstanceException {

        Users trainer = createTrainer("trainer@trainer.com");
        Users client = createClient("client@client.com");

        String clearPassword = trainer.getPassword();
        String clearClientPassword = client.getPassword();

        userService.signUp(trainer);
        userService.signUp(client);

        assertThrows(IncorrectLoginException.class, () -> userService.login(trainer.getEmail(), 'X' + clearPassword));
        assertThrows(IncorrectLoginException.class, () -> userService.login(client.getEmail(), 'X' + clearClientPassword));

    }

}
