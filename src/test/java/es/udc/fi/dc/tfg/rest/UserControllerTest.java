package es.udc.fi.dc.tfg.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.mock.web.MockMultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.udc.fi.dc.tfg.model.entities.Users;
import es.udc.fi.dc.tfg.model.entities.UserDao;
import es.udc.fi.dc.tfg.model.services.exceptions.IncorrectLoginException;
import es.udc.fi.dc.tfg.rest.controllers.UserController;
import es.udc.fi.dc.tfg.rest.dtos.AuthenticatedUserDto;
import es.udc.fi.dc.tfg.rest.dtos.ChangePasswordParamsDto;
import es.udc.fi.dc.tfg.rest.dtos.LoginParamsDto;
import es.udc.fi.dc.tfg.rest.dtos.UserDto;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

/**
 * Clase UserControllerTest.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class UserControllerTest {

    /**
     * La Constante PASSWORD.
     */
    private final static String PASSWORD = "password";

    /**
     * El mock mvc.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * El password encoder.
     */
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

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
     * Test para registrarse como entrenador.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testSignUp() throws Exception {

        UserDto trainerDto = new UserDto(null, "trainer@trainer.com", "trainer",
                "123456789", null, "TRAINER", "https://google.es", null, null,
                null, null, null);

        trainerDto.setPassword(PASSWORD);

        MockMultipartFile userPart = new MockMultipartFile("user", "", "application/json",
                new ObjectMapper().writeValueAsString(trainerDto).getBytes());
        MockMultipartFile filePart = new MockMultipartFile("file", "", "text/plain", new byte[0]);

        mockMvc.perform(multipart("/api/users/signUp")
                .file(userPart)
                .file(filePart))
                .andExpect(status().isCreated());

    }

    /**
     * Test para registrarse como entrenador sin definir un email.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testSignUp_NoEmail() throws Exception {

        UserDto trainerDto = new UserDto(null, null, "trainer",
                "123456789", "", "TRAINER", "https://google.es", null, null,
                null, null, null);

        trainerDto.setPassword(PASSWORD);

        MockMultipartFile userPart = new MockMultipartFile("user", "", "application/json",
                new ObjectMapper().writeValueAsString(trainerDto).getBytes());
        MockMultipartFile filePart = new MockMultipartFile("file", "", "text/plain", new byte[0]);

        mockMvc.perform(multipart("/api/users/signUp")
                .file(userPart)
                .file(filePart))
                .andExpect(status().isBadRequest());

    }

    /**
     * Test para registrarse como entrenador sin definir una contraseña.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testSignUp_NoPassword() throws Exception {

        UserDto trainerDto = new UserDto(null, "trainer@trainer.com", "trainer",
                "123456789", "", "TRAINER", "https://google.es", null, null,
                null, null, null);

        MockMultipartFile userPart = new MockMultipartFile("user", "", "application/json",
                new ObjectMapper().writeValueAsString(trainerDto).getBytes());
        MockMultipartFile filePart = new MockMultipartFile("file", "", "text/plain", new byte[0]);

        mockMvc.perform(multipart("/api/users/signUp")
                .file(userPart)
                .file(filePart))
                .andExpect(status().isBadRequest());

    }

    /**
     * Test para registrarse como entrenador sin definir un nombre.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testSignUp_NoName() throws Exception {

        UserDto trainerDto = new UserDto(null, "trainer@trainer.com", null,
                "123456789", "", "TRAINER", "https://google.es", null, null,
                null, null, null);

        trainerDto.setPassword(PASSWORD);

        MockMultipartFile userPart = new MockMultipartFile("user", "", "application/json",
                new ObjectMapper().writeValueAsString(trainerDto).getBytes());
        MockMultipartFile filePart = new MockMultipartFile("file", "", "text/plain", new byte[0]);

        mockMvc.perform(multipart("/api/users/signUp")
                .file(userPart)
                .file(filePart))
                .andExpect(status().isBadRequest());

    }

    /**
     * Test para registrarse como entrenador sin un rol definido.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testSignUp_NoRole() throws Exception {

        UserDto trainerDto = new UserDto(null, "trainer@trainer.com", "trainer",
                "123456789", "", null, "https://google.es", null, null, null,
                null, null);

        trainerDto.setPassword(PASSWORD);

        MockMultipartFile userPart = new MockMultipartFile("user", "", "application/json",
                new ObjectMapper().writeValueAsString(trainerDto).getBytes());
        MockMultipartFile filePart = new MockMultipartFile("file", "", "text/plain", new byte[0]);

        mockMvc.perform(multipart("/api/users/signUp")
                .file(userPart)
                .file(filePart))
                .andExpect(status().isBadRequest());

    }

    /**
     * Test para registrarse como entrenador con un email existente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testSignUp_DuplicateInstanceException() throws Exception {

        UserDto trainerDto = new UserDto(null, "trainer@trainer.com", "trainer",
                "123456789", "", "TRAINER", "https://google.es", null, null,
                null, null, null);

        trainerDto.setPassword(PASSWORD);

        MockMultipartFile userPart = new MockMultipartFile("user", "", "application/json",
                new ObjectMapper().writeValueAsString(trainerDto).getBytes());
        MockMultipartFile filePart = new MockMultipartFile("file", "", "text/plain", new byte[0]);

        mockMvc.perform(multipart("/api/users/signUp")
                .file(userPart)
                .file(filePart))
                .andExpect(status().isCreated());

        mockMvc.perform(multipart("/api/users/signUp")
                .file(userPart)
                .file(filePart))
                .andExpect(status().isBadRequest());

    }

    /**
     * Test para registrar un cliente y obtener sus datos.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testAddClientAndGetClientInfo() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();

        UserDto clientDto = new UserDto(null, "client@client.com", "client",
                "987654321", null, "CLIENT", null, "2001-09-25", "Sin lesiones",
                "Objetivo", new BigDecimal("170"), new BigDecimal("70"),
                trainerDto.getId());

        clientDto.setPassword(PASSWORD);

        MockMultipartFile userPart = new MockMultipartFile("user", "", "application/json",
                new ObjectMapper().writeValueAsString(clientDto).getBytes());
        MockMultipartFile filePart = new MockMultipartFile("file", "", "text/plain", new byte[0]);

        MockHttpServletRequestBuilder builder = multipart("/api/users/addClient")
                .file(userPart)
                .file(filePart);

        builder.header("Authorization", "Bearer " + authTrainer.getServiceToken());

        MvcResult result = mockMvc.perform(builder)
                .andExpect(status().isCreated())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        UserDto createdClient = new ObjectMapper().readValue(content, UserDto.class);

        mockMvc.perform(get("/api/users/client/" + createdClient.getId())
                .header("Authorization", "Bearer " + authTrainer.getServiceToken()))
                .andExpect(status().isOk());

    }

    /**
     * Test para registrar un cliente sin definir un email.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testAddClient_NoEmail() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();

        UserDto clientDto = new UserDto(null, null, "client",
                "987654321", null, "CLIENT", null, "2001-09-25", "Sin lesiones",
                "Objetivo", new BigDecimal("170"), null, trainerDto.getId());

        clientDto.setPassword(PASSWORD);

        MockMultipartFile userPart = new MockMultipartFile("user", "", "application/json",
                new ObjectMapper().writeValueAsString(clientDto).getBytes());
        MockMultipartFile filePart = new MockMultipartFile("file", "", "text/plain", new byte[0]);

        MockHttpServletRequestBuilder builder = multipart("/api/users/addClient")
                .file(userPart)
                .file(filePart);

        builder.header("Authorization", "Bearer " + authTrainer.getServiceToken());

        mockMvc.perform(builder)
                .andExpect(status().isBadRequest());

    }

    /**
     * Test para registrar un cliente sin definir una clave de acceso.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testAddClient_NoPassword() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();

        UserDto clientDto = new UserDto(null, "client@client.com", "client",
                "987654321", null, "CLIENT", null, "2001-09-25", "Sin lesiones",
                "Objetivo", new BigDecimal("170"), null, trainerDto.getId());

        MockMultipartFile userPart = new MockMultipartFile("user", "", "application/json",
                new ObjectMapper().writeValueAsString(clientDto).getBytes());
        MockMultipartFile filePart = new MockMultipartFile("file", "", "text/plain", new byte[0]);

        MockHttpServletRequestBuilder builder = multipart("/api/users/addClient")
                .file(userPart)
                .file(filePart);

        builder.header("Authorization", "Bearer " + authTrainer.getServiceToken());

        mockMvc.perform(builder)
                .andExpect(status().isBadRequest());

    }

    /**
     * Test para registrar un cliente sin definir un nombre.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testAddClient_NoName() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();

        UserDto clientDto = new UserDto(null, "client@client.com", null,
                "987654321", null, "CLIENT", null, "2001-09-25", "Sin lesiones",
                "Objetivo", new BigDecimal("170"), null, trainerDto.getId());

        clientDto.setPassword(PASSWORD);

        MockMultipartFile userPart = new MockMultipartFile("user", "", "application/json",
                new ObjectMapper().writeValueAsString(clientDto).getBytes());
        MockMultipartFile filePart = new MockMultipartFile("file", "", "text/plain", new byte[0]);

        MockHttpServletRequestBuilder builder = multipart("/api/users/addClient")
                .file(userPart)
                .file(filePart);

        builder.header("Authorization", "Bearer " + authTrainer.getServiceToken());

        mockMvc.perform(builder)
                .andExpect(status().isBadRequest());

    }

    /**
     * Test para registrar un cliente sin definir un rol.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testAddClient_NoRole() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();

        UserDto clientDto = new UserDto(null, "client@client.com", "client",
                "987654321", null, null, null, "2001-09-25", "Sin lesiones",
                "Objetivo", new BigDecimal("170"), null, trainerDto.getId());

        clientDto.setPassword(PASSWORD);

        MockMultipartFile userPart = new MockMultipartFile("user", "", "application/json",
                new ObjectMapper().writeValueAsString(clientDto).getBytes());
        MockMultipartFile filePart = new MockMultipartFile("file", "", "text/plain", new byte[0]);

        MockHttpServletRequestBuilder builder = multipart("/api/users/addClient")
                .file(userPart)
                .file(filePart);

        builder.header("Authorization", "Bearer " + authTrainer.getServiceToken());

        mockMvc.perform(builder)
                .andExpect(status().isBadRequest());

    }

    /**
     * Test para registrar un cliente con un email existente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testAddClient_DuplicateInstanceException() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();

        UserDto clientDto = new UserDto(null, "client@client.com", "client",
                "987654321", null, "CLIENT", null, "2001-09-25", "Sin lesiones",
                "Objetivo", new BigDecimal("170"), null, trainerDto.getId());

        clientDto.setPassword(PASSWORD);

        MockMultipartFile userPart = new MockMultipartFile("user", "", "application/json",
                new ObjectMapper().writeValueAsString(clientDto).getBytes());
        MockMultipartFile filePart = new MockMultipartFile("file", "", "text/plain", new byte[0]);

        MockHttpServletRequestBuilder builder = multipart("/api/users/addClient")
                .file(userPart)
                .file(filePart);

        builder.header("Authorization", "Bearer " + authTrainer.getServiceToken());

        mockMvc.perform(builder)
                .andExpect(status().isCreated());

        mockMvc.perform(builder)
                .andExpect(status().isBadRequest());

    }

    /**
     * Test para registrar un cliente con un trainerId inexistente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testAddClient_InstanceNotFoundException() throws Exception {

        Long incorrectUserId = -1L;

        UserDto clientDto = new UserDto(null, "client@client.com", "client",
                "987654321", null, "CLIENT", null, "2001-09-25", "Sin lesiones",
                "Objetivo", new BigDecimal("170"), null, incorrectUserId);

        clientDto.setPassword(PASSWORD);

        MockMultipartFile userPart = new MockMultipartFile("user", "", "application/json",
                new ObjectMapper().writeValueAsString(clientDto).getBytes());
        MockMultipartFile filePart = new MockMultipartFile("file", "", "text/plain", new byte[0]);

        MockHttpServletRequestBuilder builder = multipart("/api/users/addClient")
                .file(userPart)
                .file(filePart);

        builder.header("Authorization", "Bearer " + authTrainer.getServiceToken());

        mockMvc.perform(builder)
                .andExpect(status().isNotFound());

    }

    /**
     * Test para registrar un cliente con un trainerId distinto al usuario que
     * realiza la petición.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testAddClient_PermissionException() throws Exception {

        UserDto clientDto = new UserDto(null, "client@client.com", "client",
                "987654321", null, "CLIENT", null, "2001-09-25", "Sin lesiones",
                "Objetivo", new BigDecimal("170"), null, authTrainer2.getUserDto().getId());

        clientDto.setPassword(PASSWORD);

        MockMultipartFile userPart = new MockMultipartFile("user", "", "application/json",
                new ObjectMapper().writeValueAsString(clientDto).getBytes());
        MockMultipartFile filePart = new MockMultipartFile("file", "", "text/plain", new byte[0]);

        MockHttpServletRequestBuilder builder = multipart("/api/users/addClient")
                .file(userPart)
                .file(filePart);

        builder.header("Authorization", "Bearer " + authTrainer.getServiceToken());

        mockMvc.perform(builder)
                .andExpect(status().isForbidden());

    }

    /**
     * Test para registrar un cliente con un rol de cliente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testAddClient_InvalidRole() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();

        UserDto clientDto = new UserDto(null, "client@client.com", "client",
                "987654321", null, "CLIENT", null, "2001-09-25", "Sin lesiones",
                "Objetivo", new BigDecimal("170"), null, trainerDto.getId());

        clientDto.setPassword(PASSWORD);

        MockMultipartFile userPart = new MockMultipartFile("user", "", "application/json",
                new ObjectMapper().writeValueAsString(clientDto).getBytes());
        MockMultipartFile filePart = new MockMultipartFile("file", "", "text/plain", new byte[0]);

        MockHttpServletRequestBuilder builder = multipart("/api/users/addClient")
                .file(userPart)
                .file(filePart);

        builder.header("Authorization", "Bearer " + authClient.getServiceToken());

        mockMvc.perform(builder)
                .andExpect(status().isForbidden());

    }

    /**
     * Test para login.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testLogin() throws Exception {

        LoginParamsDto loginParams = new LoginParamsDto();
        loginParams.setEmail(authTrainer.getUserDto().getEmail());
        loginParams.setPassword(PASSWORD);

        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(post("/api/users/login")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(loginParams)))
                .andExpect(status().isOk());

    }

    /**
     * Test para login con service token incorrecto.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testLogin_IncorrectServiceToken() throws Exception {

        LoginParamsDto loginParams = new LoginParamsDto();
        loginParams.setEmail(authTrainer.getUserDto().getEmail());
        loginParams.setPassword(PASSWORD);

        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(post("/api/users/login")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken() + "Incorrect")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(loginParams)))
                .andExpect(status().isUnauthorized());

    }

    /**
     * Test para login con email incorrecto.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testLogin_IncorrectEmail() throws Exception {

        LoginParamsDto loginParams = new LoginParamsDto();
        loginParams.setEmail("client@trainer.com");
        loginParams.setPassword(PASSWORD);

        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(post("/api/users/login")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(loginParams)))
                .andExpect(status().isNotFound());

    }

    /**
     * Test para login con service token.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testLoginFromServiceToken() throws Exception {

        LoginParamsDto loginParams = new LoginParamsDto();
        loginParams.setEmail(authTrainer.getUserDto().getEmail());
        loginParams.setPassword(PASSWORD);

        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(post("/api/users/loginFromServiceToken")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(loginParams)))
                .andExpect(status().isOk());

    }

    /**
     * Test para login con service token con token incorrecto.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testLoginFromServiceToken_IncorrectToken() throws Exception {

        LoginParamsDto loginParams = new LoginParamsDto();
        loginParams.setEmail(authTrainer.getUserDto().getEmail());
        loginParams.setPassword(PASSWORD);

        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(post("/api/users/loginFromServiceToken")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken() + "Incorrect")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(loginParams)))
                .andExpect(status().isUnauthorized());

    }

    /**
     * Test para obtener los clientes de un entrenador.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testGetClients() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();

        mockMvc.perform(get("/api/users/" + trainerDto.getId() + "/clients")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken()))
                .andExpect(status().isOk());

    }

    /**
     * Test para obtener los clientes de un entrenador inexistente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testGetClients_InstanceNotFoundExeption() throws Exception {

        Long incorrectUserId = -1L;

        mockMvc.perform(get("/api/users/" + incorrectUserId + "/clients")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken()))
                .andExpect(status().isNotFound());

    }

    /**
     * Test para obtener los clientes sin permiso.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testGetClients_PermissionException() throws Exception {

        mockMvc.perform(get("/api/users/" + authTrainer2.getUserDto().getId() + "/clients")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken()))
                .andExpect(status().isForbidden());

    }

    /**
     * Test para obtener los clientes de un entrenador con un rol de cliente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testGetClients_InvalidRole() throws Exception {

        UserDto clientDto = authClient.getUserDto();

        mockMvc.perform(get("/api/users/" + clientDto.getId() + "/clients")
                .header("Authorization", "Bearer " + authClient.getServiceToken()))
                .andExpect(status().isForbidden());

    }

    /**
     * Test para obtener los datos de un cliente inexistente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testGetClientInfo_InstanceNotFoundExeption() throws Exception {

        Long incorrectUserId = -1L;

        mockMvc.perform(get("/api/users/client/" + incorrectUserId)
                .header("Authorization", "Bearer " + authTrainer.getServiceToken()))
                .andExpect(status().isNotFound());

    }

    /**
     * Test para obtener los datos de un cliente sin permiso.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testGetClientInfo_PermissionException() throws Exception {

        mockMvc.perform(get("/api/users/client/" + authTrainer2.getUserDto().getId())
                .header("Authorization", "Bearer " + authTrainer.getServiceToken()))
                .andExpect(status().isForbidden());

    }

    /**
     * Test para obtener los datos de un entrenador inexistente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testGetTrainerInfo_InstanceNotFoundExeption() throws Exception {

        Long incorrectUserId = -1L;

        mockMvc.perform(get("/api/users/trainer/" + incorrectUserId)
                .header("Authorization", "Bearer " + authClient.getServiceToken()))
                .andExpect(status().isNotFound());

    }

    /**
     * Test para obtener los datos de un entrenador sin permiso.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testGetTrainerInfo_PermissionException() throws Exception {

        mockMvc.perform(get("/api/users/trainer/" + authTrainer2.getUserDto().getId())
                .header("Authorization", "Bearer " + authClient.getServiceToken()))
                .andExpect(status().isForbidden());

    }

    /**
     * Test para actualizar usuario.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testUpdateProfile() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();
        UserDto clientDto = authClient.getUserDto();
        clientDto.setBirthdate(null);

        // Actualizar en caso de entrenador
        MockMultipartFile userPartT = new MockMultipartFile("user", "", "application/json",
                new ObjectMapper().writeValueAsString(trainerDto).getBytes());
        MockMultipartFile filePart = new MockMultipartFile("file", "", "text/plain", new byte[0]);

        MockHttpServletRequestBuilder builderT = multipart("/api/users/" + trainerDto.getId())
                .file(userPartT)
                .file(filePart)
                .with(request -> {
                    request.setMethod("PUT");
                    return request;
                });

        builderT.header("Authorization", "Bearer " + authTrainer.getServiceToken());

        mockMvc.perform(builderT)
                .andExpect(status().isOk());

        // Actualizar en caso de cliente
        MockMultipartFile userPartC = new MockMultipartFile("user", "", "application/json",
                new ObjectMapper().writeValueAsString(clientDto).getBytes());

        MockHttpServletRequestBuilder builderC = multipart("/api/users/" + clientDto.getId())
                .file(userPartC)
                .file(filePart)
                .with(request -> {
                    request.setMethod("PUT");
                    return request;
                });

        builderC.header("Authorization", "Bearer " + authTrainer.getServiceToken());

        mockMvc.perform(builderC)
                .andExpect(status().isOk());

        UserDto updatedClientDto = clientDto;
        updatedClientDto.setBirthdate("2005-09-25");
        updatedClientDto.setWeight(new BigDecimal("80"));

        MockMultipartFile userPartCU = new MockMultipartFile("user", "", "application/json",
                new ObjectMapper().writeValueAsString(updatedClientDto).getBytes());

        MockHttpServletRequestBuilder builderCU = multipart("/api/users/" + updatedClientDto.getId())
                .file(userPartCU)
                .file(filePart)
                .with(request -> {
                    request.setMethod("PUT");
                    return request;
                });

        builderCU.header("Authorization", "Bearer " + authTrainer.getServiceToken());

        mockMvc.perform(builderCU)
                .andExpect(status().isOk());

    }

    /**
     * Test para actualizar usuario sin definir un email.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testUpdateProfile_NoEmail() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();

        trainerDto.setEmail(null);

        MockMultipartFile userPartT = new MockMultipartFile("user", "", "application/json",
                new ObjectMapper().writeValueAsString(trainerDto).getBytes());
        MockMultipartFile filePart = new MockMultipartFile("file", "", "text/plain", new byte[0]);

        MockHttpServletRequestBuilder builderT = multipart("/api/users/" + trainerDto.getId())
                .file(userPartT)
                .file(filePart)
                .with(request -> {
                    request.setMethod("PUT");
                    return request;
                });

        builderT.header("Authorization", "Bearer " + authTrainer.getServiceToken());

        mockMvc.perform(builderT)
                .andExpect(status().isBadRequest());

    }

    /**
     * Test para actualizar usuario sin definir un nombre.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testUpdateProfile_NoName() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();

        trainerDto.setFullName(null);

        MockMultipartFile userPartT = new MockMultipartFile("user", "", "application/json",
                new ObjectMapper().writeValueAsString(trainerDto).getBytes());
        MockMultipartFile filePart = new MockMultipartFile("file", "", "text/plain", new byte[0]);

        MockHttpServletRequestBuilder builderT = multipart("/api/users/" + trainerDto.getId())
                .file(userPartT)
                .file(filePart)
                .with(request -> {
                    request.setMethod("PUT");
                    return request;
                });

        builderT.header("Authorization", "Bearer " + authTrainer.getServiceToken());

        mockMvc.perform(builderT)
                .andExpect(status().isBadRequest());

    }

    /**
     * Test para actualizar usuario sin un rol definido.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testUpdateProfile_NoRole() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();

        trainerDto.setRole(null);

        MockMultipartFile userPartT = new MockMultipartFile("user", "", "application/json",
                new ObjectMapper().writeValueAsString(trainerDto).getBytes());
        MockMultipartFile filePart = new MockMultipartFile("file", "", "text/plain", new byte[0]);

        MockHttpServletRequestBuilder builderT = multipart("/api/users/" + trainerDto.getId())
                .file(userPartT)
                .file(filePart)
                .with(request -> {
                    request.setMethod("PUT");
                    return request;
                });

        builderT.header("Authorization", "Bearer " + authTrainer.getServiceToken());

        mockMvc.perform(builderT)
                .andExpect(status().isBadRequest());

    }

    /**
     * Test para actualizar usuario con un email existente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testUpdateProfile_DuplicateInstanceException() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();

        MockMultipartFile userPartT = new MockMultipartFile("user", "", "application/json",
                new ObjectMapper().writeValueAsString(trainerDto).getBytes());
        MockMultipartFile filePart = new MockMultipartFile("file", "", "text/plain", new byte[0]);

        MockHttpServletRequestBuilder builderT = multipart("/api/users/" + trainerDto.getId())
                .file(userPartT)
                .file(filePart)
                .with(request -> {
                    request.setMethod("PUT");
                    return request;
                });

        builderT.header("Authorization", "Bearer " + authTrainer.getServiceToken());

        mockMvc.perform(builderT)
                .andExpect(status().isOk());

        UserDto authTrainerDto = authTrainer2.getUserDto();
        authTrainerDto.setEmail(trainerDto.getEmail());

        MockMultipartFile userPartA = new MockMultipartFile("user", "", "application/json",
                new ObjectMapper().writeValueAsString(authTrainerDto).getBytes());

        MockHttpServletRequestBuilder builderA = multipart("/api/users/" + authTrainerDto.getId())
                .file(userPartA)
                .file(filePart)
                .with(request -> {
                    request.setMethod("PUT");
                    return request;
                });

        builderA.header("Authorization", "Bearer " + authTrainer2.getServiceToken());

        mockMvc.perform(builderA)
                .andExpect(status().isBadRequest());

    }

    /**
     * Test para actualizar usuario inexistente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testUpdateProfile_InstanceNotFoundExeption() throws Exception {

        Long incorrectUserId = -1L;

        UserDto updatedUserDto = authTrainer.getUserDto();
        updatedUserDto.setFullName("JohnTest");

        MockMultipartFile userPartT = new MockMultipartFile("user", "", "application/json",
                new ObjectMapper().writeValueAsString(updatedUserDto).getBytes());
        MockMultipartFile filePart = new MockMultipartFile("file", "", "text/plain", new byte[0]);

        MockHttpServletRequestBuilder builderT = multipart("/api/users/" + incorrectUserId)
                .file(userPartT)
                .file(filePart)
                .with(request -> {
                    request.setMethod("PUT");
                    return request;
                });

        builderT.header("Authorization", "Bearer " + authTrainer.getServiceToken());

        mockMvc.perform(builderT)
                .andExpect(status().isNotFound());

    }

    /**
     * Test para actualizar usuario sin permiso.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testUpdateProfile_PermissionException() throws Exception {

        UserDto updatedTrainerDto = authTrainer2.getUserDto();
        updatedTrainerDto.setFullName("JohnTest");

        // Actualizar a un entrenador sin permiso
        MockMultipartFile userPartT = new MockMultipartFile("user", "", "application/json",
                new ObjectMapper().writeValueAsString(updatedTrainerDto).getBytes());
        MockMultipartFile filePart = new MockMultipartFile("file", "", "text/plain", new byte[0]);

        MockHttpServletRequestBuilder builderT = multipart("/api/users/" + updatedTrainerDto.getId())
                .file(userPartT)
                .file(filePart)
                .with(request -> {
                    request.setMethod("PUT");
                    return request;
                });

        builderT.header("Authorization", "Bearer " + authTrainer.getServiceToken());

        mockMvc.perform(builderT)
                .andExpect(status().isForbidden());

        UserDto updatedClientDto = authClient.getUserDto();
        updatedTrainerDto.setFullName("JohnTest");

        // Actualizar a un cliente sin permiso
        MockMultipartFile userPartC = new MockMultipartFile("user", "", "application/json",
                new ObjectMapper().writeValueAsString(updatedClientDto).getBytes());

        MockHttpServletRequestBuilder builderC = multipart("/api/users/" + updatedClientDto.getId())
                .file(userPartC)
                .file(filePart)
                .with(request -> {
                    request.setMethod("PUT");
                    return request;
                });

        builderT.header("Authorization", "Bearer " + authTrainer2.getServiceToken());

        mockMvc.perform(builderC)
                .andExpect(status().isForbidden());

    }

    /**
     * Test para actualizar usuario con un rol de cliente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testUpdateProfile_InvalidRole() throws Exception {

        UserDto clientDto = authClient.getUserDto();

        MockMultipartFile userPartT = new MockMultipartFile("user", "", "application/json",
                new ObjectMapper().writeValueAsString(clientDto).getBytes());
        MockMultipartFile filePart = new MockMultipartFile("file", "", "text/plain", new byte[0]);

        MockHttpServletRequestBuilder builderT = multipart("/api/users/" + clientDto.getId())
                .file(userPartT)
                .file(filePart)
                .with(request -> {
                    request.setMethod("PUT");
                    return request;
                });

        builderT.header("Authorization", "Bearer " + authClient.getServiceToken());

        mockMvc.perform(builderT)
                .andExpect(status().isForbidden());

    }

    /**
     * Test para cambiar contraseña.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testChangePassword() throws Exception {

        UserDto userDto = authTrainer.getUserDto();
        userDto.setPassword(PASSWORD);

        ChangePasswordParamsDto cpParamsDto = new ChangePasswordParamsDto();
        cpParamsDto.setOldPassword(userDto.getPassword()); // Proporciona la contraseña actual
        cpParamsDto.setNewPassword("nueva");

        mockMvc.perform(post("/api/users/" + userDto.getId() + "/changePassword")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(cpParamsDto)))
                .andExpect(status().isNoContent());

    }

    /**
     * Test para cambiar contraseña sin definir una contraseña.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testChangePassword_NoPassword() throws Exception {

        UserDto userDto = authTrainer.getUserDto();
        userDto.setPassword(PASSWORD);

        ChangePasswordParamsDto cpParamsDto = new ChangePasswordParamsDto();
        cpParamsDto.setOldPassword(userDto.getPassword()); // Proporciona la contraseña actual
        cpParamsDto.setNewPassword(null);

        mockMvc.perform(post("/api/users/" + userDto.getId() + "/changePassword")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(cpParamsDto)))
                .andExpect(status().isBadRequest());

    }

    /**
     * Test para cambiar la contraseña de un usuario inexistente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testChangePassword_InstanceNotFoundExeption() throws Exception {

        Long incorrectUserId = -1L;

        UserDto userDto = authTrainer.getUserDto();
        userDto.setPassword(PASSWORD);

        ChangePasswordParamsDto cpParamsDto = new ChangePasswordParamsDto();
        cpParamsDto.setOldPassword(userDto.getPassword()); // Proporciona la contraseña actual
        cpParamsDto.setNewPassword("nueva");

        mockMvc.perform(post("/api/users/" + incorrectUserId + "/changePassword")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(cpParamsDto)))
                .andExpect(status().isNotFound());

    }

    /**
     * Test para cambiar la contraseña sin permiso.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testChangePassword_PermissionException() throws Exception {

        UserDto userDto = authTrainer2.getUserDto();
        userDto.setPassword(PASSWORD);

        ChangePasswordParamsDto cpParamsDto = new ChangePasswordParamsDto();
        cpParamsDto.setOldPassword(userDto.getPassword()); // Proporciona la contraseña actual
        cpParamsDto.setNewPassword("nueva");

        mockMvc.perform(post("/api/users/" + userDto.getId() + "/changePassword")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(cpParamsDto)))
                .andExpect(status().isForbidden());

    }

    /**
     * Test para cambiar la contraseña con una contraseña anterior incorrecta.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testChangePassword_IncorrectPasswordException() throws Exception {

        UserDto userDto = authTrainer.getUserDto();
        userDto.setPassword(PASSWORD);

        ChangePasswordParamsDto cpParamsDto = new ChangePasswordParamsDto();
        cpParamsDto.setOldPassword("mala");
        cpParamsDto.setNewPassword("nueva");

        mockMvc.perform(post("/api/users/" + userDto.getId() + "/changePassword")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(cpParamsDto)))
                .andExpect(status().isNotFound());

    }

    /**
     * Test para cambiar la contraseña con un rol de cliente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testChangePassword_InvalidRole() throws Exception {

        UserDto clientDto = authClient.getUserDto();
        clientDto.setPassword(PASSWORD);

        ChangePasswordParamsDto cpParamsDto = new ChangePasswordParamsDto();
        cpParamsDto.setOldPassword(clientDto.getPassword()); // Proporciona la contraseña actual
        cpParamsDto.setNewPassword("nueva");

        mockMvc.perform(post("/api/users/" + clientDto.getId() + "/changePassword")
                .header("Authorization", "Bearer " + authClient.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(cpParamsDto)))
                .andExpect(status().isForbidden());

    }

    /**
     * Test para eliminar un usuario.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testDeleteUser() throws Exception {

        UserDto userDto = authTrainer.getUserDto();

        mockMvc.perform(delete("/api/users/" + userDto.getId() + "/delete")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken()))
                .andExpect(status().isOk());

    }

    /**
     * Test para eliminar un usuario inexistente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testDeleteUser_InstanceNotFoundExeption() throws Exception {

        Long incorrectUserId = -1L;

        mockMvc.perform(delete("/api/users/" + incorrectUserId + "/delete")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken()))
                .andExpect(status().isNotFound());

    }

    /**
     * Test para eliminar un usuario sin permiso.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testDeleteUser_PermissionException() throws Exception {

        mockMvc.perform(delete("/api/users/" + authTrainer2.getUserDto().getId() + "/delete")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken()))
                .andExpect(status().isForbidden());

    }

    /**
     * Test para eliminar un usuario con un rol de cliente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testDeleteUser_InvalidRole() throws Exception {

        UserDto clientDto = authClient.getUserDto();

        mockMvc.perform(delete("/api/users/" + clientDto.getId() + "/delete")
                .header("Authorization", "Bearer " + authClient.getServiceToken()))
                .andExpect(status().isForbidden());

    }

    /**
     * Test para obtener los registros de pesos de un cliente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testGetWeights() throws Exception {

        UserDto clientDto = authClient.getUserDto();

        mockMvc.perform(get("/api/users/weights/fromClient/" + clientDto.getId())
                .header("Authorization", "Bearer " + authTrainer.getServiceToken()))
                .andExpect(status().isOk());

    }

    /**
     * Test para obtener los registros de sensaciones de un cliente inexistente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testGetWeights_InstanceNotFoundExeption() throws Exception {

        Long incorrectId = -1L;

        mockMvc.perform(get("/api/users/weights/fromClient/" + incorrectId)
                .header("Authorization", "Bearer " + authTrainer.getServiceToken()))
                .andExpect(status().isNotFound());

    }

    /**
     * Test para obtener los registros de sensaciones de un cliente sin permiso.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testGetWeights_PermissionException() throws Exception {

        UserDto clientDto = authClient.getUserDto();

        mockMvc.perform(get("/api/users/weights/fromClient/" + clientDto.getId())
                .header("Authorization", "Bearer " + authTrainer2.getServiceToken()))
                .andExpect(status().isForbidden());

    }

}
