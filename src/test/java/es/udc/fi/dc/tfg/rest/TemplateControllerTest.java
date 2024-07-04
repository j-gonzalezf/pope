/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.udc.fi.dc.tfg.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.udc.fi.dc.tfg.model.entities.UserDao;
import es.udc.fi.dc.tfg.model.entities.Users;
import es.udc.fi.dc.tfg.model.services.exceptions.IncorrectLoginException;
import es.udc.fi.dc.tfg.rest.controllers.UserController;
import es.udc.fi.dc.tfg.rest.dtos.AuthenticatedUserDto;
import es.udc.fi.dc.tfg.rest.dtos.ExerciseDto;
import es.udc.fi.dc.tfg.rest.dtos.LoginParamsDto;
import es.udc.fi.dc.tfg.rest.dtos.TemplateDto;
import es.udc.fi.dc.tfg.rest.dtos.TemplateRowDto;
import es.udc.fi.dc.tfg.rest.dtos.TrainingCycleDto;
import es.udc.fi.dc.tfg.rest.dtos.UserDto;
import java.math.BigDecimal;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

/**
 * Clase TemplateControllerTest.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class TemplateControllerTest {

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
     * Test para crear una plantilla de entrenamiento.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testCreateTemplate() throws Exception {

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

        TemplateDto templateDto = new TemplateDto(null, "templateName",
                "2001-09-25T10:15:30", createdCycle.getId());

        mockMvc.perform(post("/api/templates/create")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(templateDto)))
                .andExpect(status().isCreated());

    }

    /**
     * Test para crear una plantilla de entrenamiento sin definir un nombre.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testCreateTemplate_NoName() throws Exception {

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

        TemplateDto templateDto = new TemplateDto(null, null,
                "2001-09-25T10:15:30", createdCycle.getId());

        mockMvc.perform(post("/api/templates/create")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(templateDto)))
                .andExpect(status().isBadRequest());

    }

    /**
     * Test para crear una plantilla de entrenamiento sin definir una fecha de
     * creación.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testCreateTemplate_NoCreationDate() throws Exception {

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

        TemplateDto templateDto = new TemplateDto(null, "templateName",
                null, createdCycle.getId());

        mockMvc.perform(post("/api/templates/create")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(templateDto)))
                .andExpect(status().isBadRequest());

    }

    /**
     * Test para crear una plantilla de entrenamiento con un ciclo inexistente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testCreateTemplate_InstanceNotFoundExeption() throws Exception {

        Long incorrectUserId = -1L;

        TemplateDto templateDto = new TemplateDto(null, "templateName",
                "2001-09-25T10:15:30", incorrectUserId);

        mockMvc.perform(post("/api/templates/create")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(templateDto)))
                .andExpect(status().isNotFound());

    }

    /**
     * Test para crear una plantilla de entrenamiento sin permiso.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testCreateTemplate_PermissionException() throws Exception {

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

        TemplateDto templateDto = new TemplateDto(null, "templateName",
                "2001-09-25T10:15:30", createdCycle.getId());

        mockMvc.perform(post("/api/templates/create")
                .header("Authorization", "Bearer " + authTrainer2.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(templateDto)))
                .andExpect(status().isForbidden());

    }

    /**
     * Test para crear una plantilla de entrenamiento con un rol de cliente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testCreateTemplate_InvalidRole() throws Exception {

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

        TemplateDto templateDto = new TemplateDto(null, "templateName",
                null, createdCycle.getId());

        mockMvc.perform(post("/api/templates/create")
                .header("Authorization", "Bearer " + authClient.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(templateDto)))
                .andExpect(status().isForbidden());

    }

    /**
     * Test para añadir una fila en una plantilla de entrenamiento.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testAddTemplateRow() throws Exception {

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

        ExerciseDto exerciseDto = new ExerciseDto(null, "exerciseName", "description",
                "exerciseType", null, null, null, trainerDto.getId());

        MvcResult result2 = mockMvc.perform(post("/api/exercises/exercise/add")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(exerciseDto)))
                .andExpect(status().isCreated()).andReturn();

        String content2 = result2.getResponse().getContentAsString();
        ExerciseDto createdExercise = new ObjectMapper().readValue(content2, ExerciseDto.class);

        TemplateDto templateDto = new TemplateDto(null, "templateName",
                "2001-09-25T10:15:30", createdCycle.getId());

        MvcResult result3 = mockMvc.perform(post("/api/templates/create")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(templateDto)))
                .andExpect(status().isCreated()).andReturn();

        String content3 = result3.getResponse().getContentAsString();
        TemplateDto createdTemplate = new ObjectMapper().readValue(content3, TemplateDto.class);

        TemplateRowDto templateRowDto = new TemplateRowDto(null, 3, 10, new BigDecimal("50"),
                createdExercise.getId(), createdTemplate.getId());

        mockMvc.perform(post("/api/templates/addRow")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(templateRowDto)))
                .andExpect(status().isCreated());

    }

    /**
     * Test para añadir una fila en una plantilla inexistente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testCreateTemplateRow_InstanceNotFoundExeption() throws Exception {

        Long incorrectUserId = -1L;

        TemplateRowDto templateRowDto = new TemplateRowDto(null, 3, 10, new BigDecimal("50"),
                incorrectUserId, incorrectUserId);

        mockMvc.perform(post("/api/templates/addRow")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(templateRowDto)))
                .andExpect(status().isNotFound());

    }

    /**
     * Test para aladur una fila en una plantilla sin permiso.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testCreateTemplateRow_PermissionException() throws Exception {

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

        ExerciseDto exerciseDto = new ExerciseDto(null, "exerciseName", "description",
                "exerciseType", null, null, null, trainerDto.getId());

        MvcResult result2 = mockMvc.perform(post("/api/exercises/exercise/add")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(exerciseDto)))
                .andExpect(status().isCreated()).andReturn();

        String content2 = result2.getResponse().getContentAsString();
        ExerciseDto createdExercise = new ObjectMapper().readValue(content2, ExerciseDto.class);

        TemplateDto templateDto = new TemplateDto(null, "templateName",
                "2001-09-25T10:15:30", createdCycle.getId());

        MvcResult result3 = mockMvc.perform(post("/api/templates/create")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(templateDto)))
                .andExpect(status().isCreated()).andReturn();

        String content3 = result3.getResponse().getContentAsString();
        TemplateDto createdTemplate = new ObjectMapper().readValue(content3, TemplateDto.class);

        TemplateRowDto templateRowDto = new TemplateRowDto(null, 3, 10, new BigDecimal("50"),
                createdExercise.getId(), createdTemplate.getId());

        mockMvc.perform(post("/api/templates/addRow")
                .header("Authorization", "Bearer " + authTrainer2.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(templateRowDto)))
                .andExpect(status().isForbidden());

    }

    /**
     * Test para añadir una fila en una plantilla con un rol de cliente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testCreateTemplateRow_InvalidRole() throws Exception {

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

        ExerciseDto exerciseDto = new ExerciseDto(null, "exerciseName", "description",
                "exerciseType", null, null, null, trainerDto.getId());

        MvcResult result2 = mockMvc.perform(post("/api/exercises/exercise/add")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(exerciseDto)))
                .andExpect(status().isCreated()).andReturn();

        String content2 = result2.getResponse().getContentAsString();
        ExerciseDto createdExercise = new ObjectMapper().readValue(content2, ExerciseDto.class);

        TemplateDto templateDto = new TemplateDto(null, "templateName",
                "2001-09-25T10:15:30", createdCycle.getId());

        MvcResult result3 = mockMvc.perform(post("/api/templates/create")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(templateDto)))
                .andExpect(status().isCreated()).andReturn();

        String content3 = result3.getResponse().getContentAsString();
        TemplateDto createdTemplate = new ObjectMapper().readValue(content3, TemplateDto.class);

        TemplateRowDto templateRowDto = new TemplateRowDto(null, 3, 10, new BigDecimal("50"),
                createdExercise.getId(), createdTemplate.getId());

        mockMvc.perform(post("/api/templates/addRow")
                .header("Authorization", "Bearer " + authClient.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(templateRowDto)))
                .andExpect(status().isForbidden());

    }

}
