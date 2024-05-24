package es.udc.fi.dc.tfg.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.udc.fi.dc.tfg.model.entities.UserDao;
import es.udc.fi.dc.tfg.model.entities.Users;
import es.udc.fi.dc.tfg.model.services.exceptions.IncorrectLoginException;
import es.udc.fi.dc.tfg.rest.controllers.UserController;
import es.udc.fi.dc.tfg.rest.dtos.AuthenticatedUserDto;
import es.udc.fi.dc.tfg.rest.dtos.LoginParamsDto;
import es.udc.fi.dc.tfg.rest.dtos.TrainingCycleDto;
import es.udc.fi.dc.tfg.rest.dtos.UserDto;

/**
 * Clase TrainingCycleControllerTest.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class TrainingCycleControllerTest {

    /**
     * La Constante PASSWORD.
     */
    private final static String PASSWORD = "password";

    /**
     * El password encoder.
     */
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * El mock mvc.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * El user dao.
     */
    @Autowired
    private UserDao userDao;

    /**
     * El user controller.
     */
    @Autowired
    private UserController userController;

    /**
     * El authenticated user dto.
     */
    private AuthenticatedUserDto authTrainer;
    private AuthenticatedUserDto authClient;

    @Before
    public void createEntities() throws IncorrectLoginException {
        authTrainer = createAuthenticatedTrainer("trainer@user.com");
        authClient = createAuthenticatedClient("client@user.com");
    }

    /**
     * Crea el authenticated trainer.
     *
     * @param email el email
     * @return el authenticated user dto
     * @throws IncorrectLoginException si el email o la contraseña son
     * incorrectos.
     */
    private AuthenticatedUserDto createAuthenticatedTrainer(String email)
            throws IncorrectLoginException {

        Users trainer = new Users(email, PASSWORD, "trainer", "123456789", "", "");

        trainer.setPassword(passwordEncoder.encode(trainer.getPassword()));

        userDao.save(trainer);

        LoginParamsDto loginParams = new LoginParamsDto();
        loginParams.setEmail(trainer.getEmail());
        loginParams.setPassword(PASSWORD);

        return userController.login(loginParams);

    }

    /**
     * Crea el authenticated client.
     *
     * @param email el email
     * @return el authenticated user dto
     * @throws IncorrectLoginException si el email o la contraseña son
     * incorrectos.
     */
    private AuthenticatedUserDto createAuthenticatedClient(String email)
            throws IncorrectLoginException {

        Users trainer = new Users(authTrainer.getUserDto().getEmail(), PASSWORD,
                "trainer", "123456789", "", "");

        trainer.setPassword(passwordEncoder.encode(trainer.getPassword()));
        trainer.setId(authTrainer.getUserDto().getId());

        Users client = new Users(email, PASSWORD, "trainer", "123456789", "", null,
                null, null, null, trainer);

        client.setPassword(passwordEncoder.encode(client.getPassword()));

        userDao.save(client);

        LoginParamsDto loginParams = new LoginParamsDto();
        loginParams.setEmail(client.getEmail());
        loginParams.setPassword(PASSWORD);

        return userController.login(loginParams);

    }

    /**
     * Test para crear un ciclo de entrenamiento.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testCreateTrainingCycle() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();

        UserDto clientDto = authClient.getUserDto();

        TrainingCycleDto cycleDto = new TrainingCycleDto(null, "cycleName", null,
                "2001-09-25", "2002-09-25", trainerDto.getId(), clientDto.getId());

        mockMvc.perform(post("/api/templates/cycle/create")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(cycleDto)))
                .andExpect(status().isCreated()).andReturn();

    }

}
