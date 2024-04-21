package es.udc.fi.dc.tfg.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
//import es.udc.fi.dc.tfg.model.entities.Users.RoleType;
import es.udc.fi.dc.tfg.model.entities.UserDao;
import es.udc.fi.dc.tfg.model.services.exceptions.IncorrectLoginException;
import es.udc.fi.dc.tfg.rest.controllers.UserController;
import es.udc.fi.dc.tfg.rest.dtos.AuthenticatedUserDto;
import es.udc.fi.dc.tfg.rest.dtos.LoginParamsDto;
import es.udc.fi.dc.tfg.rest.dtos.UserDto;
import java.math.BigDecimal;
import java.time.LocalDate;

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
     * Creates the authenticated user.
     *
     * @param userName the user name
     * @param roleType the role type
     * @return the authenticated user dto
     * @throws IncorrectLoginException the incorrect login exception
     */
/*    
    private AuthenticatedUserDto createAuthenticatedUser(String userName, RoleType roleType)
            throws IncorrectLoginException {

        Users user = new Users(userName, PASSWORD, "newUser", "user", "user@test.com");

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(roleType);

        userDao.save(user);

        LoginParamsDto loginParams = new LoginParamsDto();
        loginParams.setUserName(user.getUserName());
        loginParams.setPassword(PASSWORD);

        return userController.login(loginParams);

    }
*/    

    @Test
    public void testSignUp() throws Exception {
        
        UserDto trainerDto = new UserDto(null, "trainer@trainer.com", "trainer", 
                "123456789", "", "TRAINER", "", null, null, null, null);
        
        UserDto clientDto = new UserDto(null, "client@client.com", "client", 
                "987654321", "", "CLIENT", null, LocalDate.of(2000,1,2), 
                "Sin lesiones", "Objetivo", new BigDecimal("170"));
        
        trainerDto.setPassword(PASSWORD);
        clientDto.setPassword(PASSWORD);
/*
        mockMvc.perform(post("/api/users/signUp").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(trainerDto))).andExpect(status().isCreated());
        
        mockMvc.perform(post("/api/users/signUp").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(clientDto))).andExpect(status().isCreated());
*/       
    }

    @Test
    public void testSignUp_DuplicatedUser() throws Exception {
        
        UserDto trainerDto = new UserDto(null, "trainer@trainer.com", "trainer", 
                "123456789", "", "TRAINER", "", null, null, null, null);
        
        trainerDto.setPassword(PASSWORD);
/*
        mockMvc.perform(post("/api/users/signUp").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(trainerDto))).andExpect(status().isCreated());
*/
        mockMvc.perform(post("/api/users/signUp").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(trainerDto))).andExpect(status().isBadRequest());
        
    }

    @Test
    public void testSignUp_NotPassword() throws Exception {
            
        UserDto trainerDto = new UserDto(null, "trainer@trainer.com", "trainer", 
                "123456789", "", "TRAINER", "", null, null, null, null);

        mockMvc.perform(post("/api/users/signUp").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(trainerDto))).andExpect(status().isBadRequest());
        
    }
    
    @Test
    public void testSignUp_InvalidRole() throws Exception {
    
        UserDto trainerDto = new UserDto(null, "trainer@trainer.com", "trainer", 
                "123456789", "", null, "", null, null, null, null);
        
        trainerDto.setPassword(PASSWORD);
        
        mockMvc.perform(post("/api/users/signUp").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(trainerDto))).andExpect(status().isBadRequest());
        
    }
    
    /**
     * Test post login ok.
     *
     * @throws Exception the exception
     */
    /*
	@Test
	public void testPostLogin_Ok() throws Exception {

		AuthenticatedUserDto user = createAuthenticatedUser("admin", RoleType.USER);

		LoginParamsDto loginParams = new LoginParamsDto();
		loginParams.setUserName(user.getUserDto().getUserName());
		loginParams.setPassword(PASSWORD);

		ObjectMapper mapper = new ObjectMapper();

		mockMvc.perform(post("/api/users/login").header("Authorization", "Bearer " + user.getServiceToken())
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(loginParams)))
				.andExpect(status().isOk());

	}
     */
}
