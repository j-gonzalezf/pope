package es.udc.fi.dc.tfg.model.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class UsersTest {

    private Users user;
    private Users trainer;
    private Users client;

    @Before
    public void setUp() {
        user = new Users();
        trainer = new Users("trainer@example.com", "password", "Test Trainer",
                "1234567890", null, null);
        client = new Users("client@example.com", "password", "Test Client",
                "1234567890", null, null, null, null, null, null);
    }

    @Test
    public void testUsersConstructors() {

        // Test user constructor
        assertNotNull(user);
        assertNull(user.getId());

        // Test trainer constructor
        assertNotNull(trainer);
        assertEquals("trainer@example.com", trainer.getEmail());
        assertEquals("Test Trainer", trainer.getFullName());
        assertEquals(Users.RoleType.TRAINER, trainer.getUserRole());

        // Test client constructor
        assertNotNull(client);
        assertEquals("client@example.com", client.getEmail());
        assertEquals("Test Client", client.getFullName());
        assertEquals(Users.RoleType.CLIENT, client.getUserRole());

    }

    @Test
    public void testUsersGettersAndSetters() {

        // Test setters
        trainer.setId(3L);
        trainer.setEmail("new@example.com");
        trainer.setPassword("newPassword");
        trainer.setFullName("New User");
        trainer.setPhone("123456789");
        trainer.setIcon("user.jpg");
        trainer.setSocialLinks("https://linktr.ee/");

        client.setBirthdate(LocalDate.of(2000, 1, 1));
        client.setInjuries("None");
        client.setGoals("Fitness");
        client.setHeight(new BigDecimal("170"));
        client.setTrainer(trainer);

        // Test getters
        assertEquals(3L, trainer.getId().longValue());
        assertEquals("new@example.com", trainer.getEmail());
        assertEquals("newPassword", trainer.getPassword());
        assertEquals("New User", trainer.getFullName());
        assertEquals("123456789", trainer.getPhone());
        assertEquals("user.jpg", trainer.getIcon());
        assertEquals("https://linktr.ee/", trainer.getSocialLinks());

        assertEquals(LocalDate.of(2000, 1, 1), client.getBirthdate());
        assertEquals("None", client.getInjuries());
        assertEquals("Fitness", client.getGoals());
        assertEquals(new BigDecimal("170"), client.getHeight());
        assertEquals(trainer, client.getTrainer());

    }

}
