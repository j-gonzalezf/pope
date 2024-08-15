package es.udc.fi.dc.tfg.rest;

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
import es.udc.fi.dc.tfg.rest.dtos.SensationDto;
import es.udc.fi.dc.tfg.rest.dtos.TemplateDto;
import es.udc.fi.dc.tfg.rest.dtos.TrainingCycleDto;
import es.udc.fi.dc.tfg.rest.dtos.UserDto;

/**
 * Clase SensationControllerTest.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class SensationControllerTest {

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
    private AuthenticatedUserDto authClient2;

    @Before
    public void createEntities() throws IncorrectLoginException {
        authTrainer = createAuthenticatedTrainer("trainer@user.com");
        authClient = createAuthenticatedClient("client@user.com");
        authClient2 = createAuthenticatedClient("client2@user.com");
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
     * Test para crear un registro de sensaciones.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testSensationsRegister() throws Exception {

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

        MvcResult result3 = mockMvc.perform(post("/api/templates/create")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(templateDto)))
                .andExpect(status().isCreated()).andReturn();

        String content3 = result3.getResponse().getContentAsString();
        TemplateDto createdTemplate = new ObjectMapper().readValue(content3, TemplateDto.class);

        SensationDto sensationDto = new SensationDto(null, 2, 3, 4, 5, "2002-09-25T10:15:30",
                createdTemplate.getId(), clientDto.getId());

        mockMvc.perform(post("/api/sensations/create")
                .header("Authorization", "Bearer " + authClient.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(sensationDto)))
                .andExpect(status().isCreated());

    }

    /**
     * Test para crear un registro de sensaciones en una plantilla inexistente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testSensationsRegister_InstanceNotFoundExeption() throws Exception {

        Long incorrectUserId = -1L;

        SensationDto sensationDto = new SensationDto(null, 2, 3, 4, 5, "2002-09-25T10:15:30",
                incorrectUserId, authClient.getUserDto().getId());

        mockMvc.perform(post("/api/sensations/create")
                .header("Authorization", "Bearer " + authClient.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(sensationDto)))
                .andExpect(status().isNotFound());

    }

    /**
     * Test para crear un registro de sensaciones sin permiso.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testSensationsRegister_PermissionException() throws Exception {

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

        MvcResult result3 = mockMvc.perform(post("/api/templates/create")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(templateDto)))
                .andExpect(status().isCreated()).andReturn();

        String content3 = result3.getResponse().getContentAsString();
        TemplateDto createdTemplate = new ObjectMapper().readValue(content3, TemplateDto.class);

        SensationDto sensationDto = new SensationDto(null, 2, 3, 4, 5, "2002-09-25T10:15:30",
                createdTemplate.getId(), clientDto.getId());

        mockMvc.perform(post("/api/sensations/create")
                .header("Authorization", "Bearer " + authClient2.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(sensationDto)))
                .andExpect(status().isForbidden());

    }

    /**
     * Test para crear un registro de sensaciones con un rol de entrenador.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testSensationsRegister_InvalidRole() throws Exception {

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

        MvcResult result3 = mockMvc.perform(post("/api/templates/create")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(templateDto)))
                .andExpect(status().isCreated()).andReturn();

        String content3 = result3.getResponse().getContentAsString();
        TemplateDto createdTemplate = new ObjectMapper().readValue(content3, TemplateDto.class);

        SensationDto sensationDto = new SensationDto(null, 2, 3, 4, 5, "2002-09-25T10:15:30",
                createdTemplate.getId(), trainerDto.getId());

        mockMvc.perform(post("/api/sensations/create")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(sensationDto)))
                .andExpect(status().isForbidden());

    }

    /**
     * Test para editar el resgistro de sensaciones de una plantilla.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testUpdateSensations() throws Exception {

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

        MvcResult result3 = mockMvc.perform(post("/api/templates/create")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(templateDto)))
                .andExpect(status().isCreated()).andReturn();

        String content3 = result3.getResponse().getContentAsString();
        TemplateDto createdTemplate = new ObjectMapper().readValue(content3, TemplateDto.class);

        SensationDto sensationDto = new SensationDto(null, 2, 3, 4, 5, "2002-09-25T10:15:30",
                createdTemplate.getId(), clientDto.getId());

        MvcResult result4 = mockMvc.perform(post("/api/sensations/create")
                .header("Authorization", "Bearer " + authClient.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(sensationDto)))
                .andExpect(status().isCreated()).andReturn();

        String content4 = result4.getResponse().getContentAsString();
        SensationDto newSensations = new ObjectMapper().readValue(content4, SensationDto.class);

        mockMvc.perform(put("/api/sensations/" + newSensations.getId() + "/update")
                .header("Authorization", "Bearer " + authClient.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(newSensations)))
                .andExpect(status().isOk());

    }

    /**
     * Test para editar un resgistro de sensaciones inexistente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testUpdateSensations_InstanceNotFoundExeption() throws Exception {

        Long incorrectId = -1L;
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

        MvcResult result3 = mockMvc.perform(post("/api/templates/create")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(templateDto)))
                .andExpect(status().isCreated()).andReturn();

        String content3 = result3.getResponse().getContentAsString();
        TemplateDto createdTemplate = new ObjectMapper().readValue(content3, TemplateDto.class);

        SensationDto sensationDto = new SensationDto(null, 2, 3, 4, 5, "2002-09-25T10:15:30",
                createdTemplate.getId(), clientDto.getId());

        MvcResult result4 = mockMvc.perform(post("/api/sensations/create")
                .header("Authorization", "Bearer " + authClient.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(sensationDto)))
                .andExpect(status().isCreated()).andReturn();

        String content4 = result4.getResponse().getContentAsString();
        SensationDto newSensations = new ObjectMapper().readValue(content4, SensationDto.class);

        mockMvc.perform(put("/api/sensations/" + incorrectId + "/update")
                .header("Authorization", "Bearer " + authClient.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(newSensations)))
                .andExpect(status().isNotFound());

    }

    /**
     * Test para editar un registro de sensaciones sin permiso.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testUpdateSensations_PermissionException() throws Exception {

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

        MvcResult result3 = mockMvc.perform(post("/api/templates/create")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(templateDto)))
                .andExpect(status().isCreated()).andReturn();

        String content3 = result3.getResponse().getContentAsString();
        TemplateDto createdTemplate = new ObjectMapper().readValue(content3, TemplateDto.class);

        SensationDto sensationDto = new SensationDto(null, 2, 3, 4, 5, "2002-09-25T10:15:30",
                createdTemplate.getId(), clientDto.getId());

        MvcResult result4 = mockMvc.perform(post("/api/sensations/create")
                .header("Authorization", "Bearer " + authClient.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(sensationDto)))
                .andExpect(status().isCreated()).andReturn();

        String content4 = result4.getResponse().getContentAsString();
        SensationDto newSensations = new ObjectMapper().readValue(content4, SensationDto.class);

        mockMvc.perform(put("/api/sensations/" + newSensations.getId() + "/update")
                .header("Authorization", "Bearer " + authClient2.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(newSensations)))
                .andExpect(status().isForbidden());

    }

    /**
     * Test para editar un registro de sensaciones con un rol de entrenador.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testUpdateSensations_InvalidRole() throws Exception {

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

        MvcResult result3 = mockMvc.perform(post("/api/templates/create")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(templateDto)))
                .andExpect(status().isCreated()).andReturn();

        String content3 = result3.getResponse().getContentAsString();
        TemplateDto createdTemplate = new ObjectMapper().readValue(content3, TemplateDto.class);

        SensationDto sensationDto = new SensationDto(null, 2, 3, 4, 5, "2002-09-25T10:15:30",
                createdTemplate.getId(), clientDto.getId());

        MvcResult result4 = mockMvc.perform(post("/api/sensations/create")
                .header("Authorization", "Bearer " + authClient.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(sensationDto)))
                .andExpect(status().isCreated()).andReturn();

        String content4 = result4.getResponse().getContentAsString();
        SensationDto newSensations = new ObjectMapper().readValue(content4, SensationDto.class);
        newSensations.setClientId(authTrainer.getUserDto().getId());

        mockMvc.perform(put("/api/sensations/" + newSensations.getId() + "/update")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(newSensations)))
                .andExpect(status().isForbidden());

    }

}
