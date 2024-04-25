package es.udc.fi.dc.tfg.rest;

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
    private AuthenticatedUserDto authUser;

    @Before
    public void createEntities() throws IncorrectLoginException {
        authUser = createAuthenticatedUser("user@user.com");
    }

    /**
     * Creates the authenticated user.
     *
     * @param userName the user name
     * @param roleType the role type
     * @return the authenticated user dto
     * @throws IncorrectLoginException the incorrect login exception
     */
    private AuthenticatedUserDto createAuthenticatedUser(String email)
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

        UserDto clientDto = new UserDto(null, "client@client.com", "client",
                "987654321", null, "CLIENT", null, "2001-09-25", "Sin lesiones",
                "Objetivo", new BigDecimal("170"), trainerDto.getId());

        clientDto.setPassword(PASSWORD);

        mockMvc.perform(post("/api/users/signUp")
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(clientDto)))
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
     * Test para login.
     *
     * @throws Exception the exception
     */
    @Test
    public void testLogin() throws Exception {

        LoginParamsDto loginParams = new LoginParamsDto();
        loginParams.setEmail(authUser.getUserDto().getEmail());
        loginParams.setPassword(PASSWORD);

        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(post("/api/users/login")
                .header("Authorization", "Bearer " + authUser.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(loginParams)))
                .andExpect(status().isOk());

    }

    @Test
    public void testLoginIncorrectServiceToken() throws Exception {

        LoginParamsDto loginParams = new LoginParamsDto();
        loginParams.setEmail(authUser.getUserDto().getEmail());
        loginParams.setPassword(PASSWORD);

        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(post("/api/users/login")
                .header("Authorization", "Bearer " + authUser.getServiceToken() + "Incorrect")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(loginParams)))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void testLoginIncorrectEmail() throws Exception {

        LoginParamsDto loginParams = new LoginParamsDto();
        loginParams.setEmail("client@trainer.com");
        loginParams.setPassword(PASSWORD);

        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(post("/api/users/login").header("Authorization", "Bearer " + authUser.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(loginParams)))
                .andExpect(status().isNotFound());

    }

    @Test
    public void testLoginFromServiceToken() throws Exception {

        LoginParamsDto loginParams = new LoginParamsDto();
        loginParams.setEmail(authUser.getUserDto().getEmail());
        loginParams.setPassword(PASSWORD);

        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(post("/api/users/loginFromServiceToken")
                .header("Authorization", "Bearer " + authUser.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(loginParams)))
                .andExpect(status().isOk());

    }

    @Test
    public void testLoginFromServiceTokenIncorrectToken() throws Exception {

        LoginParamsDto loginParams = new LoginParamsDto();
        loginParams.setEmail(authUser.getUserDto().getEmail());
        loginParams.setPassword(PASSWORD);

        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(post("/api/users/loginFromServiceToken")
                .header("Authorization", "Bearer " + authUser.getServiceToken() + "Incorrect")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(loginParams)))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void testChangePassword() throws Exception {

        UserDto userDto = authUser.getUserDto();
        userDto.setPassword(PASSWORD);

        ChangePasswordParamsDto changePasswordParamsDto = new ChangePasswordParamsDto();
        changePasswordParamsDto.setOldPassword(userDto.getPassword()); // Proporciona la contraseña actual
        changePasswordParamsDto.setNewPassword("nueva");

        mockMvc.perform(post("/api/users/" + userDto.getId() + "/changePassword", userDto.getId())
                .header("Authorization", "Bearer " + authUser.getServiceToken()).contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(changePasswordParamsDto))
                .header("userId", userDto.getId().toString())).andExpect(status().isNoContent());

    }

    @Test
    public void testChangePasswordIncorrect() throws Exception {

        Long incorrectUserId = -1L;

        UserDto updatedUserDto = authUser.getUserDto();
        updatedUserDto.setFullName("JohnTest");

        mockMvc.perform(put("/api/users/" + updatedUserDto.getId() + "/changePassword", incorrectUserId)
                .header("Authorization", "Bearer " + authUser.getServiceToken()).contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updatedUserDto))
                .header("userId", incorrectUserId.toString())).andExpect(status().isMethodNotAllowed());

    }

    @Test
    public void testChangePasswordIncorrectPasswordException() throws Exception {

        UserDto userDto = authUser.getUserDto();
        userDto.setPassword(PASSWORD);

        ChangePasswordParamsDto changePasswordParamsDto = new ChangePasswordParamsDto();
        changePasswordParamsDto.setOldPassword("mala");
        changePasswordParamsDto.setNewPassword("nueva");

        mockMvc.perform(post("/api/users/" + userDto.getId() + "/changePassword", userDto.getId())
                .header("Authorization", "Bearer " + authUser.getServiceToken()).contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(changePasswordParamsDto))
                .header("userId", userDto.getId().toString())).andExpect(status().isNotFound());

    }

}
