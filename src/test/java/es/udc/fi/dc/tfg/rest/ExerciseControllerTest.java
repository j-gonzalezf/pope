package es.udc.fi.dc.tfg.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.test.web.servlet.MvcResult;
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
    private AuthenticatedUserDto authTrainer2;
    private AuthenticatedUserDto authClient;

    @Before
    public void createEntities() throws IncorrectLoginException {
        authTrainer = createAuthenticatedTrainer("trainer@user.com");
        authTrainer2 = createAuthenticatedTrainer("trainer2@user.com");
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
     * Test para añadir un ejercicio y obtenerlo a partir de su ID..
     *
     * @throws Exception la excepción
     */
    @Test
    public void testAddExerciseAndGetExercise() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();

        ExerciseDto exerciseDto = new ExerciseDto(null, "exerciseName", "description",
                "exerciseType", null, null, null, trainerDto.getId());

        MvcResult result = mockMvc.perform(post("/api/exercises/exercise/add")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(exerciseDto)))
                .andExpect(status().isCreated()).andReturn();

        String content = result.getResponse().getContentAsString();
        ExerciseDto createdExercise = new ObjectMapper().readValue(content, ExerciseDto.class);

        mockMvc.perform(get("/api/exercises/exercise/" + createdExercise.getId())
                .header("Authorization", "Bearer " + authTrainer.getServiceToken()))
                .andExpect(status().isOk());

    }

    /**
     * Test para añadir un ejercicio sin definir un nombre.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testAddExercise_NoName() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();

        ExerciseDto exerciseDto = new ExerciseDto(null, null, "description",
                "exerciseType", null, null, null, trainerDto.getId());

        mockMvc.perform(post("/api/exercises/exercise/add")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(exerciseDto)))
                .andExpect(status().isBadRequest());

    }

    /**
     * Test para añadir un ejercicio sin definir una descripción.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testAddExercise_NoDescription() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();

        ExerciseDto exerciseDto = new ExerciseDto(null, "exerciseName", null,
                "exerciseType", null, null, null, trainerDto.getId());

        mockMvc.perform(post("/api/exercises/exercise/add")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(exerciseDto)))
                .andExpect(status().isBadRequest());

    }

    /**
     * Test para añadir un ejercicio sin definir una categoría.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testAddExercise_NoType() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();

        ExerciseDto exerciseDto = new ExerciseDto(null, "exerciseName", "description",
                null, null, null, null, trainerDto.getId());

        mockMvc.perform(post("/api/exercises/exercise/add")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(exerciseDto)))
                .andExpect(status().isBadRequest());

    }

    /**
     * Test para añadir un ejercicio con un usuario inexistente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testCreateTrainingCycle_InstanceNotFoundExeption() throws Exception {

        Long incorrectUserId = -1L;

        ExerciseDto exerciseDto = new ExerciseDto(null, "exerciseName", "description",
                "exerciseType", null, null, null, incorrectUserId);

        mockMvc.perform(post("/api/exercises/exercise/add")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(exerciseDto)))
                .andExpect(status().isNotFound());

    }

    /**
     * Test para añadir un ejercicio sin permiso.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testCreateTrainingCycle_PermissionException() throws Exception {

        UserDto trainerDto = authTrainer2.getUserDto();

        ExerciseDto exerciseDto = new ExerciseDto(null, "exerciseName", "description",
                "exerciseType", null, null, null, trainerDto.getId());

        mockMvc.perform(post("/api/exercises/exercise/add")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(exerciseDto)))
                .andExpect(status().isForbidden());

    }

    /**
     * Test para añadir un ejercicio con un rol de cliente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testCreateTrainingCycle_InvalidRole() throws Exception {

        UserDto clientDto = authClient.getUserDto();

        ExerciseDto exerciseDto = new ExerciseDto(null, "exerciseName", "description",
                "exerciseType", null, null, null, clientDto.getId());

        mockMvc.perform(post("/api/exercises/exercise/add")
                .header("Authorization", "Bearer " + authClient.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(exerciseDto)))
                .andExpect(status().isForbidden());

    }

    /**
     * Test para obtener los ejercicios añadidos por un entrenador.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testGetExercises() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();

        mockMvc.perform(get("/api/exercises/" + trainerDto.getId())
                .header("Authorization", "Bearer " + authTrainer.getServiceToken()))
                .andExpect(status().isOk());

    }

    /**
     * Test para obtener los ejercicios de un entrenador con un usuario
     * inexistente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testGetExercises_InstanceNotFoundExeption() throws Exception {

        Long incorrectUserId = -1L;

        mockMvc.perform(get("/api/exercises/" + incorrectUserId)
                .header("Authorization", "Bearer " + authTrainer.getServiceToken()))
                .andExpect(status().isNotFound());

    }

    /**
     * Test para obtener los ejercicios de un entrenador sin permiso.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testGetExercises_PermissionException() throws Exception {

        UserDto trainerDto = authTrainer2.getUserDto();

        mockMvc.perform(get("/api/exercises/" + trainerDto.getId())
                .header("Authorization", "Bearer " + authTrainer.getServiceToken()))
                .andExpect(status().isForbidden());

    }

    /**
     * Test para obtener los ejercicios de un entrenador con un rol de cliente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testGetExercises_InvalidRole() throws Exception {

        UserDto clientDto = authClient.getUserDto();

        mockMvc.perform(get("/api/exercises/" + clientDto.getId())
                .header("Authorization", "Bearer " + authClient.getServiceToken()))
                .andExpect(status().isForbidden());

    }

    /**
     * Test para editar un ejercicio.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testUpdateExercise() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();

        ExerciseDto exerciseDto = new ExerciseDto(null, "exerciseName", "description",
                "exerciseType", null, null, null, trainerDto.getId());

        MvcResult result = mockMvc.perform(post("/api/exercises/exercise/add")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(exerciseDto)))
                .andExpect(status().isCreated()).andReturn();

        String content = result.getResponse().getContentAsString();
        ExerciseDto newExerciseDto = new ObjectMapper().readValue(content, ExerciseDto.class);

        mockMvc.perform(put("/api/exercises/" + newExerciseDto.getId())
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(newExerciseDto)))
                .andExpect(status().isOk());

    }

    /**
     * Test para editar un ejercicio sin definir un nombre.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testUpdateExercise_NoName() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();

        ExerciseDto exerciseDto = new ExerciseDto(null, "exerciseName", "description",
                "exerciseType", null, null, null, trainerDto.getId());

        MvcResult result = mockMvc.perform(post("/api/exercises/exercise/add")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(exerciseDto)))
                .andExpect(status().isCreated()).andReturn();

        String content = result.getResponse().getContentAsString();
        ExerciseDto newExerciseDto = new ObjectMapper().readValue(content, ExerciseDto.class);
        newExerciseDto.setName(null);

        mockMvc.perform(put("/api/exercises/" + newExerciseDto.getId())
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(newExerciseDto)))
                .andExpect(status().isBadRequest());

    }

    /**
     * Test para editar un ejercicio sin definir una descripción.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testUpdateExercise_NoDescription() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();

        ExerciseDto exerciseDto = new ExerciseDto(null, "exerciseName", "description",
                "exerciseType", null, null, null, trainerDto.getId());

        MvcResult result = mockMvc.perform(post("/api/exercises/exercise/add")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(exerciseDto)))
                .andExpect(status().isCreated()).andReturn();

        String content = result.getResponse().getContentAsString();
        ExerciseDto newExerciseDto = new ObjectMapper().readValue(content, ExerciseDto.class);
        newExerciseDto.setDescription(null);

        mockMvc.perform(put("/api/exercises/" + newExerciseDto.getId())
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(newExerciseDto)))
                .andExpect(status().isBadRequest());

    }

    /**
     * Test para editar un ejercicio sin definir una categoría.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testUpdateExercise_NoType() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();

        ExerciseDto exerciseDto = new ExerciseDto(null, "exerciseName", "description",
                "exerciseType", null, null, null, trainerDto.getId());

        MvcResult result = mockMvc.perform(post("/api/exercises/exercise/add")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(exerciseDto)))
                .andExpect(status().isCreated()).andReturn();

        String content = result.getResponse().getContentAsString();
        ExerciseDto newExerciseDto = new ObjectMapper().readValue(content, ExerciseDto.class);
        newExerciseDto.setType(null);

        mockMvc.perform(put("/api/exercises/" + newExerciseDto.getId())
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(newExerciseDto)))
                .andExpect(status().isBadRequest());

    }

    /**
     * Test para editar un ejercicio inexistente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testUpdateExercise_InstanceNotFoundExeption() throws Exception {

        Long incorrectUserId = -1L;
        UserDto trainerDto = authTrainer.getUserDto();

        ExerciseDto exerciseDto = new ExerciseDto(null, "exerciseName", "description",
                "exerciseType", null, null, null, trainerDto.getId());

        mockMvc.perform(put("/api/exercises/" + incorrectUserId)
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(exerciseDto)))
                .andExpect(status().isNotFound());

    }

    /**
     * Test para editar un ejercicio sin permiso.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testUpdateExercise_PermissionException() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();

        ExerciseDto exerciseDto = new ExerciseDto(null, "exerciseName", "description",
                "exerciseType", null, null, null, trainerDto.getId());

        MvcResult result = mockMvc.perform(post("/api/exercises/exercise/add")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(exerciseDto)))
                .andExpect(status().isCreated()).andReturn();

        String content = result.getResponse().getContentAsString();
        ExerciseDto newExerciseDto = new ObjectMapper().readValue(content, ExerciseDto.class);

        mockMvc.perform(put("/api/exercises/" + newExerciseDto.getId())
                .header("Authorization", "Bearer " + authTrainer2.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(newExerciseDto)))
                .andExpect(status().isForbidden());

    }

    /**
     * Test para editar un ejercicio con un rol de cliente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testUpdateExercise_InvalidRole() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();

        ExerciseDto exerciseDto = new ExerciseDto(null, "exerciseName", "description",
                "exerciseType", null, null, null, trainerDto.getId());

        MvcResult result = mockMvc.perform(post("/api/exercises/exercise/add")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(exerciseDto)))
                .andExpect(status().isCreated()).andReturn();

        String content = result.getResponse().getContentAsString();
        ExerciseDto newExerciseDto = new ObjectMapper().readValue(content, ExerciseDto.class);

        mockMvc.perform(put("/api/exercises/" + newExerciseDto.getId())
                .header("Authorization", "Bearer " + authClient.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(newExerciseDto)))
                .andExpect(status().isForbidden());

    }

    /**
     * Test para eliminar un ejercicio.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testDeleteExercise() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();

        ExerciseDto exerciseDto = new ExerciseDto(null, "exerciseName", "description",
                "exerciseType", null, null, null, trainerDto.getId());

        MvcResult result = mockMvc.perform(post("/api/exercises/exercise/add")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(exerciseDto)))
                .andExpect(status().isCreated()).andReturn();

        String content = result.getResponse().getContentAsString();
        ExerciseDto newExerciseDto = new ObjectMapper().readValue(content, ExerciseDto.class);

        mockMvc.perform(delete("/api/exercises/" + newExerciseDto.getId() + "/delete")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken()))
                .andExpect(status().isOk());

    }

    /**
     * Test para eliminar un ejercicio inexistente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testDeleteExercise_InstanceNotFoundExeption() throws Exception {

        Long incorrectUserId = -1L;

        mockMvc.perform(delete("/api/exercises/" + incorrectUserId + "/delete")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken()))
                .andExpect(status().isNotFound());

    }

    /**
     * Test para eliminar un ejercicio sin permiso.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testDeleteExercise_PermissionException() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();

        ExerciseDto exerciseDto = new ExerciseDto(null, "exerciseName", "description",
                "exerciseType", null, null, null, trainerDto.getId());

        MvcResult result = mockMvc.perform(post("/api/exercises/exercise/add")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(exerciseDto)))
                .andExpect(status().isCreated()).andReturn();

        String content = result.getResponse().getContentAsString();
        ExerciseDto newExerciseDto = new ObjectMapper().readValue(content, ExerciseDto.class);

        mockMvc.perform(delete("/api/exercises/" + newExerciseDto.getId() + "/delete")
                .header("Authorization", "Bearer " + authTrainer2.getServiceToken()))
                .andExpect(status().isForbidden());

    }

    /**
     * Test para eliminar un ejercicio con un rol de cliente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testDeleteExercise_InvalidRole() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();

        ExerciseDto exerciseDto = new ExerciseDto(null, "exerciseName", "description",
                "exerciseType", null, null, null, trainerDto.getId());

        MvcResult result = mockMvc.perform(post("/api/exercises/exercise/add")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(exerciseDto)))
                .andExpect(status().isCreated()).andReturn();

        String content = result.getResponse().getContentAsString();
        ExerciseDto newExerciseDto = new ObjectMapper().readValue(content, ExerciseDto.class);

        mockMvc.perform(delete("/api/exercises/" + newExerciseDto.getId() + "/delete")
                .header("Authorization", "Bearer " + authClient.getServiceToken()))
                .andExpect(status().isForbidden());

    }

}
