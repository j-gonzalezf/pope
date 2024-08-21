package es.udc.fi.dc.tfg.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.udc.fi.dc.tfg.model.entities.UserDao;
import es.udc.fi.dc.tfg.model.entities.Users;
import es.udc.fi.dc.tfg.model.services.exceptions.IncorrectLoginException;
import es.udc.fi.dc.tfg.rest.controllers.UserController;
import es.udc.fi.dc.tfg.rest.dtos.AuthenticatedUserDto;
import es.udc.fi.dc.tfg.rest.dtos.CommentDto;
import es.udc.fi.dc.tfg.rest.dtos.LoginParamsDto;
import es.udc.fi.dc.tfg.rest.dtos.TemplateDto;
import es.udc.fi.dc.tfg.rest.dtos.TrainingCycleDto;
import es.udc.fi.dc.tfg.rest.dtos.UserDto;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

/**
 * Clase CommentControllerTest.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class CommentControllerTest {

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
     * Test para escribir comentarios.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testWriteComment() throws Exception {

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

        CommentDto commentDtoT = new CommentDto(null, "trainer comment", "2002-09-25T10:15:30",
                createdTemplate.getId(), trainerDto.getId());

        CommentDto commentDtoC = new CommentDto(null, "client comment", "2002-09-25T10:15:30",
                createdTemplate.getId(), clientDto.getId());

        // Comentar como entrenador
        mockMvc.perform(post("/api/comments/write")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(commentDtoT)))
                .andExpect(status().isCreated());

        // Comentar como cliente
        mockMvc.perform(post("/api/comments/write")
                .header("Authorization", "Bearer " + authClient.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(commentDtoC)))
                .andExpect(status().isCreated());

    }

    /**
     * Test para escribir un comentario sin permiso.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testWriteComment_PermissionException() throws Exception {

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

        CommentDto commentDto = new CommentDto(null, "client comment", "2002-09-25T10:15:30",
                createdTemplate.getId(), clientDto.getId());

        mockMvc.perform(post("/api/comments/write")
                .header("Authorization", "Bearer " + authClient2.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(commentDto)))
                .andExpect(status().isForbidden());

    }

}
