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
import es.udc.fi.dc.tfg.rest.dtos.ExerciseDto;
import es.udc.fi.dc.tfg.rest.dtos.LoginParamsDto;
import es.udc.fi.dc.tfg.rest.dtos.UserDto;

/**
 * Clase ExerciseControllerTest.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ExerciseControllerTest {

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

    @Before
    public void createEntities() throws IncorrectLoginException {
        authTrainer = createAuthenticatedTrainer("trainer@user.com");
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
     * Test para añadir un ejercicio.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testAddExercise() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();

        ExerciseDto exerciseDto = new ExerciseDto(null, "exerciseName", "description",
                "exerciseType", null, null, null, trainerDto.getId());

        mockMvc.perform(post("/api/exercises/exercise/add")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(exerciseDto)))
                .andExpect(status().isCreated()).andReturn();

    }

}
