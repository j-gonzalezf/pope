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
     * Test para crear un ciclo de entrenamiento y obtenerlo a partir de su ID.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testCreateTrainingCycleAndGetCycle() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();
        UserDto clientDto = authClient.getUserDto();

        TrainingCycleDto cycleDto = new TrainingCycleDto(null, "cycleName", null,
                "2001-09-25", "2002-09-25", trainerDto.getId(), clientDto.getId());

        MvcResult result = mockMvc.perform(post("/api/templates/cycle/create")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(cycleDto)))
                .andExpect(status().isCreated()).andReturn();

        String content = result.getResponse().getContentAsString();
        TrainingCycleDto createdCycle = new ObjectMapper().readValue(content, TrainingCycleDto.class);

        mockMvc.perform(get("/api/templates/cycle/" + createdCycle.getId())
                .header("Authorization", "Bearer " + authTrainer.getServiceToken()))
                .andExpect(status().isOk());

    }

    /**
     * Test para crear un ciclo de entrenamiento sin definir un nombre.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testCreateTrainingCycle_NotName() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();
        UserDto clientDto = authClient.getUserDto();

        TrainingCycleDto cycleDto = new TrainingCycleDto(null, null, null,
                "2001-09-25", "2002-09-25", trainerDto.getId(), clientDto.getId());

        mockMvc.perform(post("/api/templates/cycle/create")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(cycleDto)))
                .andExpect(status().isBadRequest());

    }

    /**
     * Test para crear un ciclo de entrenamiento sin definir una fecha de inici.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testCreateTrainingCycle_NotFromDate() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();
        UserDto clientDto = authClient.getUserDto();

        TrainingCycleDto cycleDto = new TrainingCycleDto(null, "cycleName", null,
                null, "2002-09-25", trainerDto.getId(), clientDto.getId());

        mockMvc.perform(post("/api/templates/cycle/create")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(cycleDto)))
                .andExpect(status().isBadRequest());

    }

    /**
     * Test para crear un ciclo de entrenamiento sin definir una fecha de fin.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testCreateTrainingCycle_NotToDate() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();
        UserDto clientDto = authClient.getUserDto();

        TrainingCycleDto cycleDto = new TrainingCycleDto(null, "cycleName", null,
                "2001-09-25", null, trainerDto.getId(), clientDto.getId());

        mockMvc.perform(post("/api/templates/cycle/create")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(cycleDto)))
                .andExpect(status().isBadRequest());

    }

    /**
     * Test para crear un ciclo de entrenamiento con un usuario inexistente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testCreateTrainingCycle_InstanceNotFoundExeption() throws Exception {

        Long incorrectUserId = -1L;
        UserDto trainerDto = authTrainer.getUserDto();
        UserDto clientDto = authClient.getUserDto();

        TrainingCycleDto cycleWithNoTrainer = new TrainingCycleDto(null, "cycleName",
                null, "2001-09-25", "2002-09-25", incorrectUserId, clientDto.getId());

        TrainingCycleDto cycleWithNoClient = new TrainingCycleDto(null, "cycleName",
                null, "2001-09-25", "2002-09-25", trainerDto.getId(), incorrectUserId);

        // Crear un ciclo con un entrenador que no existe
        mockMvc.perform(post("/api/templates/cycle/create")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(cycleWithNoTrainer)))
                .andExpect(status().isNotFound());

        // Crear un ciclo con un cliente que no existe
        mockMvc.perform(post("/api/templates/cycle/create")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(cycleWithNoClient)))
                .andExpect(status().isNotFound());

    }

    /**
     * Test para crear un ciclo de entrenamiento sin permiso.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testCreateTrainingCycle_PermissionException() throws Exception {

        UserDto trainerDto = authTrainer2.getUserDto();
        UserDto clientDto = authClient.getUserDto();

        TrainingCycleDto cycleDto = new TrainingCycleDto(null, "cycleName", null,
                "2001-09-25", "2002-09-25", trainerDto.getId(), clientDto.getId());

        mockMvc.perform(post("/api/templates/cycle/create")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(cycleDto)))
                .andExpect(status().isForbidden());

    }

    /**
     * Test para crear un ciclo de entrenamiento con un rol de cliente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testCreateTrainingCycle_InvalidRole() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();
        UserDto clientDto = authClient.getUserDto();

        TrainingCycleDto cycleDto = new TrainingCycleDto(null, "cycleName", null,
                "2001-09-25", "2002-09-25", trainerDto.getId(), clientDto.getId());

        mockMvc.perform(post("/api/templates/cycle/create")
                .header("Authorization", "Bearer " + authClient.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(cycleDto)))
                .andExpect(status().isForbidden());

    }

    /**
     * Test para obtener los ciclos del cliente de un entrenador.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testGetTrainingCycles() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();
        UserDto clientDto = authClient.getUserDto();

        mockMvc.perform(get("/api/templates/" + trainerDto.getId() + "/clients/" + clientDto.getId() + "/cycles")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken()))
                .andExpect(status().isOk());

    }

    /**
     * Test para obtener los ciclos del cliente de un entrenador con un usuario
     * inexistente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testGetTrainingCycles_InstanceNotFoundExeption() throws Exception {

        Long incorrectUserId = -1L;
        UserDto trainerDto = authTrainer.getUserDto();
        UserDto clientDto = authClient.getUserDto();

        // Solicitar la obtención de un ciclo con un entrenador que no existe
        mockMvc.perform(get("/api/templates/" + incorrectUserId + "/clients/" + clientDto.getId() + "/cycles")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken()))
                .andExpect(status().isNotFound());

        // Solicitar la obtención de un ciclo con un cliente que no existe
        mockMvc.perform(get("/api/templates/" + trainerDto.getId() + "/clients/" + incorrectUserId + "/cycles")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken()))
                .andExpect(status().isNotFound());

    }

    /**
     * Test para obtener los ciclos del cliente de un entrenador sin permiso.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testGetTrainingCycles_PermissionException() throws Exception {

        UserDto trainerDto = authTrainer2.getUserDto();
        UserDto clientDto = authClient.getUserDto();

        mockMvc.perform(get("/api/templates/" + trainerDto.getId() + "/clients/" + clientDto.getId() + "/cycles")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken()))
                .andExpect(status().isForbidden());

    }

    /**
     * Test para obtener los ciclos del cliente de un entrenador con un rol de
     * cliente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testGetTrainingCycles_InvalidRole() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();
        UserDto clientDto = authClient.getUserDto();

        mockMvc.perform(get("/api/templates/" + trainerDto.getId() + "/clients/" + clientDto.getId() + "/cycles")
                .header("Authorization", "Bearer " + authClient.getServiceToken()))
                .andExpect(status().isForbidden());

    }

    /**
     * Test para obtener un ciclo a partir de ID inexistente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testGetCycle_InstanceNotFoundExeption() throws Exception {

        Long incorrectUserId = -1L;

        mockMvc.perform(get("/api/templates/cycle/" + incorrectUserId)
                .header("Authorization", "Bearer " + authTrainer.getServiceToken()))
                .andExpect(status().isNotFound());

    }

    /**
     * Test para obtener un ciclo a partir de ID sin permiso.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testGetCycle_PermissionException() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();
        UserDto clientDto = authClient.getUserDto();

        TrainingCycleDto cycleDto = new TrainingCycleDto(null, "cycleName", null,
                "2001-09-25", "2002-09-25", trainerDto.getId(), clientDto.getId());

        MvcResult result = mockMvc.perform(post("/api/templates/cycle/create")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(cycleDto)))
                .andExpect(status().isCreated()).andReturn();

        String content = result.getResponse().getContentAsString();
        TrainingCycleDto createdCycle = new ObjectMapper().readValue(content, TrainingCycleDto.class);

        mockMvc.perform(get("/api/templates/cycle/" + createdCycle.getId())
                .header("Authorization", "Bearer " + authTrainer2.getServiceToken()))
                .andExpect(status().isForbidden());

    }

    /**
     * Test para obtener un ciclo a partir de su ID con un rol de cliente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testGetCycle_InvalidRole() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();
        UserDto clientDto = authClient.getUserDto();

        TrainingCycleDto cycleDto = new TrainingCycleDto(null, "cycleName", null,
                "2001-09-25", "2002-09-25", trainerDto.getId(), clientDto.getId());

        MvcResult result = mockMvc.perform(post("/api/templates/cycle/create")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(cycleDto)))
                .andExpect(status().isCreated()).andReturn();

        String content = result.getResponse().getContentAsString();
        TrainingCycleDto createdCycle = new ObjectMapper().readValue(content, TrainingCycleDto.class);

        mockMvc.perform(get("/api/templates/cycle/" + createdCycle.getId())
                .header("Authorization", "Bearer " + authClient.getServiceToken()))
                .andExpect(status().isForbidden());

    }

    /**
     * Test para editar un ciclo de entrenamiento.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testUpdateTrainingCycle() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();
        UserDto clientDto = authClient.getUserDto();

        TrainingCycleDto cycleDto = new TrainingCycleDto(null, "cycleName", null,
                "2001-09-25", "2002-09-25", trainerDto.getId(), clientDto.getId());

        MvcResult result = mockMvc.perform(post("/api/templates/cycle/create")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(cycleDto)))
                .andExpect(status().isCreated()).andReturn();

        String content = result.getResponse().getContentAsString();
        TrainingCycleDto newCycleDto = new ObjectMapper().readValue(content, TrainingCycleDto.class);

        mockMvc.perform(put("/api/templates/" + newCycleDto.getId())
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(newCycleDto)))
                .andExpect(status().isOk());

    }

    /**
     * Test para editar un ciclo de entrenamiento sin definir un nombre.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testUpdateTrainingCycle_NotName() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();
        UserDto clientDto = authClient.getUserDto();

        TrainingCycleDto cycleDto = new TrainingCycleDto(null, "cycleName", null,
                "2001-09-25", "2002-09-25", trainerDto.getId(), clientDto.getId());

        MvcResult result = mockMvc.perform(post("/api/templates/cycle/create")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(cycleDto)))
                .andExpect(status().isCreated()).andReturn();

        String content = result.getResponse().getContentAsString();
        TrainingCycleDto updatedCycleDto = new ObjectMapper().readValue(content, TrainingCycleDto.class);
        updatedCycleDto.setName(null);

        mockMvc.perform(put("/api/templates/" + updatedCycleDto.getId())
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(updatedCycleDto)))
                .andExpect(status().isBadRequest());

    }

    /**
     * Test para crear un editar de entrenamiento sin definir una fecha de
     * inici.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testUpdateTrainingCycle_NotFromDate() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();
        UserDto clientDto = authClient.getUserDto();

        TrainingCycleDto cycleDto = new TrainingCycleDto(null, "cycleName", null,
                "2001-09-25", "2002-09-25", trainerDto.getId(), clientDto.getId());

        MvcResult result = mockMvc.perform(post("/api/templates/cycle/create")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(cycleDto)))
                .andExpect(status().isCreated()).andReturn();

        String content = result.getResponse().getContentAsString();
        TrainingCycleDto updatedCycleDto = new ObjectMapper().readValue(content, TrainingCycleDto.class);
        updatedCycleDto.setFromDate(null);

        mockMvc.perform(put("/api/templates/" + updatedCycleDto.getId())
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(updatedCycleDto)))
                .andExpect(status().isBadRequest());

    }

    /**
     * Test para editar un ciclo de entrenamiento sin definir una fecha de fin.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testUpdateTrainingCycle_NotToDate() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();
        UserDto clientDto = authClient.getUserDto();

        TrainingCycleDto cycleDto = new TrainingCycleDto(null, "cycleName", null,
                "2001-09-25", "2002-09-25", trainerDto.getId(), clientDto.getId());

        MvcResult result = mockMvc.perform(post("/api/templates/cycle/create")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(cycleDto)))
                .andExpect(status().isCreated()).andReturn();

        String content = result.getResponse().getContentAsString();
        TrainingCycleDto updatedCycleDto = new ObjectMapper().readValue(content, TrainingCycleDto.class);
        updatedCycleDto.setToDate(null);

        mockMvc.perform(put("/api/templates/" + updatedCycleDto.getId())
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(updatedCycleDto)))
                .andExpect(status().isBadRequest());

    }

    /**
     * Test para editar un ciclo de entrenamiento inexistente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testUpdateTrainingCycle_InstanceNotFoundExeption() throws Exception {

        Long incorrectUserId = -1L;
        UserDto trainerDto = authTrainer.getUserDto();
        UserDto clientDto = authClient.getUserDto();

        TrainingCycleDto cycleDto = new TrainingCycleDto(null, "cycleName",
                null, "2001-09-25", "2002-09-25", trainerDto.getId(), clientDto.getId());

        mockMvc.perform(put("/api/templates/" + incorrectUserId)
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(cycleDto)))
                .andExpect(status().isNotFound());

    }

    /**
     * Test para editar un ciclo de entrenamiento sin permiso.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testUpdateTrainingCycle_PermissionException() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();
        UserDto clientDto = authClient.getUserDto();

        TrainingCycleDto cycleDto = new TrainingCycleDto(null, "cycleName", null,
                "2001-09-25", "2002-09-25", trainerDto.getId(), clientDto.getId());

        MvcResult result = mockMvc.perform(post("/api/templates/cycle/create")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(cycleDto)))
                .andExpect(status().isCreated()).andReturn();

        String content = result.getResponse().getContentAsString();
        TrainingCycleDto updatedCycleDto = new ObjectMapper().readValue(content, TrainingCycleDto.class);

        mockMvc.perform(put("/api/templates/" + updatedCycleDto.getId())
                .header("Authorization", "Bearer " + authTrainer2.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(updatedCycleDto)))
                .andExpect(status().isForbidden());

    }

    /**
     * Test para editar un ciclo de entrenamiento con un rol de cliente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testUpdateTrainingCycle_InvalidRole() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();
        UserDto clientDto = authClient.getUserDto();

        TrainingCycleDto cycleDto = new TrainingCycleDto(null, "cycleName", null,
                "2001-09-25", "2002-09-25", trainerDto.getId(), clientDto.getId());

        MvcResult result = mockMvc.perform(post("/api/templates/cycle/create")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(cycleDto)))
                .andExpect(status().isCreated()).andReturn();

        String content = result.getResponse().getContentAsString();
        TrainingCycleDto updatedCycleDto = new ObjectMapper().readValue(content, TrainingCycleDto.class);

        mockMvc.perform(put("/api/templates/" + updatedCycleDto.getId())
                .header("Authorization", "Bearer " + authClient.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(updatedCycleDto)))
                .andExpect(status().isForbidden());

    }

    /**
     * Test para eliminar un ciclo.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testDeleteCycle() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();
        UserDto clientDto = authClient.getUserDto();

        TrainingCycleDto cycleDto = new TrainingCycleDto(null, "cycleName", null,
                "2001-09-25", "2002-09-25", trainerDto.getId(), clientDto.getId());

        MvcResult result = mockMvc.perform(post("/api/templates/cycle/create")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(cycleDto)))
                .andExpect(status().isCreated()).andReturn();

        String content = result.getResponse().getContentAsString();
        TrainingCycleDto createdCycle = new ObjectMapper().readValue(content, TrainingCycleDto.class);

        mockMvc.perform(delete("/api/templates/" + createdCycle.getId() + "/delete")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken()))
                .andExpect(status().isOk());

    }

    /**
     * Test para eliminar un ciclo inexistente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testDeleteCycle_InstanceNotFoundExeption() throws Exception {

        Long incorrectUserId = -1L;

        mockMvc.perform(delete("/api/templates/" + incorrectUserId + "/delete")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken()))
                .andExpect(status().isNotFound());

    }

    /**
     * Test para eliminar un ciclo sin permiso.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testDeleteCycle_PermissionException() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();
        UserDto clientDto = authClient.getUserDto();

        TrainingCycleDto cycleDto = new TrainingCycleDto(null, "cycleName", null,
                "2001-09-25", "2002-09-25", trainerDto.getId(), clientDto.getId());

        MvcResult result = mockMvc.perform(post("/api/templates/cycle/create")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(cycleDto)))
                .andExpect(status().isCreated()).andReturn();

        String content = result.getResponse().getContentAsString();
        TrainingCycleDto createdCycle = new ObjectMapper().readValue(content, TrainingCycleDto.class);

        mockMvc.perform(delete("/api/templates/" + createdCycle.getId() + "/delete")
                .header("Authorization", "Bearer " + authTrainer2.getServiceToken()))
                .andExpect(status().isForbidden());

    }

    /**
     * Test para eliminar un ciclo con un rol de cliente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testDeleteCycle_InvalidRole() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();
        UserDto clientDto = authClient.getUserDto();

        TrainingCycleDto cycleDto = new TrainingCycleDto(null, "cycleName", null,
                "2001-09-25", "2002-09-25", trainerDto.getId(), clientDto.getId());

        MvcResult result = mockMvc.perform(post("/api/templates/cycle/create")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(cycleDto)))
                .andExpect(status().isCreated()).andReturn();

        String content = result.getResponse().getContentAsString();
        TrainingCycleDto createdCycle = new ObjectMapper().readValue(content, TrainingCycleDto.class);

        mockMvc.perform(delete("/api/templates/" + createdCycle.getId() + "/delete")
                .header("Authorization", "Bearer " + authClient.getServiceToken()))
                .andExpect(status().isForbidden());

    }

}
