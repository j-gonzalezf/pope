package es.udc.fi.dc.tfg.model.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
public class WeightsTest {

    private Weights voidWeight;
    private Weights weight;
    private Users trainer;
    private Users client;
    private Users newClient;

    @Before
    public void setUp() {
        voidWeight = new Weights();
        trainer = new Users("trainer@example.com", "password", "Test Trainer",
                "1234567890", null, null);
        client = new Users("client@example.com", "password", "Test Client",
                "1234567890", null, null, null, null, null, trainer);
        newClient = new Users("newClient@example.com", "password", "Test Client",
                "1234567890", null, null, null, null, null, trainer);
        weight = new Weights(new BigDecimal("30"),
                LocalDateTime.of(2001, 1, 1, 0, 0), client);
    }

    @Test
    public void testWeightsConstructors() {

        // Test template constructor
        assertNotNull(voidWeight);
        assertNull(voidWeight.getId());

        // Test template constructor
        assertNotNull(weight);
        assertEquals(new BigDecimal("30"), weight.getWeight());
        assertEquals(LocalDateTime.of(2001, 1, 1, 0, 0), weight.getWeightDate());
        assertEquals(client, weight.getClient());

    }

    @Test
    public void testWeightGettersAndSetters() {

        // Test setters
        weight.setId(3L);
        weight.setWeight(new BigDecimal("50"));
        weight.setWeightDate(LocalDateTime.of(2000, 2, 1, 0, 0));
        weight.setClient(newClient);

        // Test getters
        assertEquals(3L, weight.getId().longValue());
        assertEquals(new BigDecimal("50"), weight.getWeight());
        assertEquals(LocalDateTime.of(2000, 2, 1, 0, 0), weight.getWeightDate());
        assertEquals(newClient, weight.getClient());

    }

}
