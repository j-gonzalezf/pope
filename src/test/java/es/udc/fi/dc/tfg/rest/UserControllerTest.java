package es.udc.fi.dc.tfg.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import es.udc.fi.dc.tfg.model.entities.Users;
import es.udc.fi.dc.tfg.model.entities.UserDao;
import es.udc.fi.dc.tfg.model.services.exceptions.IncorrectLoginException;
import es.udc.fi.dc.tfg.rest.controllers.UserController;
import es.udc.fi.dc.tfg.rest.dtos.AuthenticatedUserDto;
import es.udc.fi.dc.tfg.rest.dtos.ChangePasswordParamsDto;
import es.udc.fi.dc.tfg.rest.dtos.LoginParamsDto;
import es.udc.fi.dc.tfg.rest.dtos.UserDto;
import java.math.BigDecimal;
import org.junit.Before;

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

        Users user = new Users(email, PASSWORD, "trainer", "123456789", "", "");

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userDao.save(user);

        LoginParamsDto loginParams = new LoginParamsDto();
        loginParams.setEmail(user.getEmail());
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

        Users trainer = new Users("t" + email, PASSWORD, "trainer", "123456789", "", "");

        trainer.setPassword(passwordEncoder.encode(trainer.getPassword()));

        userDao.save(trainer);

        Users user = new Users(email, PASSWORD, "trainer", "123456789", "", null,
                null, null, null, trainer);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userDao.save(user);

        LoginParamsDto loginParams = new LoginParamsDto();
        loginParams.setEmail(user.getEmail());
        loginParams.setPassword(PASSWORD);

        return userController.login(loginParams);

    }

    /**
     * Test para registrarse como usuario.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testSignUp() throws Exception {

        UserDto trainerDto = new UserDto(null, "trainer@trainer.com", "trainer",
                "123456789", null, "TRAINER", "https://google.es", null, null,
                null, null, null);

        trainerDto.setPassword(PASSWORD);

        mockMvc.perform(post("/api/users/signUp")
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(trainerDto)))
                .andExpect(status().isCreated());

    }

    /**
     * Test para registrarse como usuario con un email existente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testSignUp_DuplicatedUser() throws Exception {

        UserDto trainerDto = new UserDto(null, "trainer@trainer.com", "trainer",
                "123456789", "", "TRAINER", "https://google.es", null, null,
                null, null, null);

        trainerDto.setPassword(PASSWORD);

        mockMvc.perform(post("/api/users/signUp")
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(trainerDto)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/users/signUp")
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(trainerDto)))
                .andExpect(status().isBadRequest());

    }

    /**
     * Test para registrarse como usuario sin definir una contraseña.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testSignUp_NotPassword() throws Exception {

        UserDto trainerDto = new UserDto(null, "trainer@trainer.com", "trainer",
                "123456789", "", "TRAINER", "https://google.es", null, null,
                null, null, null);

        mockMvc.perform(post("/api/users/signUp")
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(trainerDto)))
                .andExpect(status().isBadRequest());

    }

    /**
     * Test para registrarse como usuario sin un rol definido.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testSignUp_InvalidRole() throws Exception {

        UserDto trainerDto = new UserDto(null, "trainer@trainer.com", "trainer",
                "123456789", "", null, "https://google.es", null, null, null,
                null, null);

        trainerDto.setPassword(PASSWORD);

        mockMvc.perform(post("/api/users/signUp")
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(trainerDto)))
                .andExpect(status().isBadRequest());

    }

    /**
     * Test para registrar un cliente y mostrar sus datos.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testAddClientAndGetClientInfo() throws Exception {

        UserDto trainerDto = authTrainer.getUserDto();

        UserDto clientDto = new UserDto(null, "client@client.com", "client",
                "987654321", null, "CLIENT", null, "2001-09-25", "Sin lesiones",
                "Objetivo", new BigDecimal("170"), trainerDto.getId());
        
        clientDto.setPassword(PASSWORD);

        MvcResult result = mockMvc.perform(post("/api/users/addClient")
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(clientDto)))
                .andExpect(status().isCreated()).andReturn();

        String content = result.getResponse().getContentAsString();
        UserDto createdClient = new ObjectMapper().readValue(content, UserDto.class);

        mockMvc.perform(get("/api/users/client/" + createdClient.getId())
                .header("Authorization", "Bearer " + authTrainer.getServiceToken()))
                .andExpect(status().isOk());

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
                "Objetivo", new BigDecimal("170"), trainerDto.getId());

        clientDto.setPassword(PASSWORD);

        mockMvc.perform(post("/api/users/addClient")
                .header("Authorization", "Bearer " + authClient.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(clientDto)))
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
     * Test para actualizar perfil.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testUpdateProfile() throws Exception {

        UserDto userDto = authTrainer.getUserDto();

        mockMvc.perform(put("/api/users/" + userDto.getId(), userDto.getId())
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(userDto))
                .header("userId", userDto.getId().toString()))
                .andExpect(status().isOk());

    }

    /**
     * Test para actualizar perfil con usuario inexistente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testUpdateProfile_Incorrect() throws Exception {

        Long incorrectUserId = -1L;

        UserDto updatedUserDto = authTrainer.getUserDto();
        updatedUserDto.setFullName("JohnTest");

        mockMvc.perform(put("/api/users/" + incorrectUserId, updatedUserDto.getId())
                .header("Authorization", "Bearer " + authTrainer.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(updatedUserDto))
                .header("userId", updatedUserDto.getId().toString())).andExpect(status().isNotFound());

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

        ChangePasswordParamsDto changePasswordParamsDto = new ChangePasswordParamsDto();
        changePasswordParamsDto.setOldPassword(userDto.getPassword()); // Proporciona la contraseña actual
        changePasswordParamsDto.setNewPassword("nueva");

        mockMvc.perform(post("/api/users/" + userDto.getId() + "/changePassword", userDto.getId())
                .header("Authorization", "Bearer " + authTrainer.getServiceToken()).contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(changePasswordParamsDto))
                .header("userId", userDto.getId().toString())).andExpect(status().isNoContent());

    }

    /**
     * Test para cambiar la contraseña con un usuario inexistente.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testChangePassword_Incorrect() throws Exception {

        Long incorrectUserId = -1L;

        UserDto updatedUserDto = authTrainer.getUserDto();
        updatedUserDto.setFullName("JohnTest");

        mockMvc.perform(put("/api/users/" + updatedUserDto.getId() + "/changePassword", incorrectUserId)
                .header("Authorization", "Bearer " + authTrainer.getServiceToken()).contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updatedUserDto))
                .header("userId", incorrectUserId.toString())).andExpect(status().isMethodNotAllowed());

    }

    /**
     * Test para cambiar la contraseña con una contraseña incorrecta.
     *
     * @throws Exception la excepción
     */
    @Test
    public void testChangePassword_IncorrectPasswordException() throws Exception {

        UserDto userDto = authTrainer.getUserDto();
        userDto.setPassword(PASSWORD);

        ChangePasswordParamsDto changePasswordParamsDto = new ChangePasswordParamsDto();
        changePasswordParamsDto.setOldPassword("mala");
        changePasswordParamsDto.setNewPassword("nueva");

        mockMvc.perform(post("/api/users/" + userDto.getId() + "/changePassword", userDto.getId())
                .header("Authorization", "Bearer " + authTrainer.getServiceToken()).contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(changePasswordParamsDto))
                .header("userId", userDto.getId().toString())).andExpect(status().isNotFound());

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

}
