package es.udc.fi.dc.tfg.model.entities;

import java.time.LocalDate;
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
public class CommentsTest {

    private Comments voidComments;
    private Comments comments;
    private Templates template;
    private Templates newTemplate;
    private TrainingCycles trainingCycle;
    private Users trainer;
    private Users client;
    private Users newClient;

    @Before
    public void setUp() {
        voidComments = new Comments();
        trainer = new Users("trainer@example.com", "password", "Test Trainer",
                "1234567890", null, null);
        client = new Users("client@example.com", "password", "Test Client",
                "1234567890", null, null, null, null, null, trainer);
        newClient = new Users("newClient@example.com", "password", "Test Client",
                "1234567890", null, null, null, null, null, trainer);
        trainingCycle = new TrainingCycles("cycleName", null,
                LocalDate.of(2000, 1, 1), LocalDate.of(2001, 1, 1), trainer, client);
        template = new Templates("templateName",
                LocalDateTime.of(2001, 1, 1, 0, 0), trainingCycle);
        newTemplate = new Templates("templateName",
                LocalDateTime.of(2001, 1, 1, 0, 0), trainingCycle);
        comments = new Comments("comment", LocalDateTime.of(2001, 1, 1, 0, 0),
                template, client);
    }

    @Test
    public void testCommentsConstructors() {

        // Test void comments constructor
        assertNotNull(voidComments);
        assertNull(voidComments.getId());

        // Test comments constructor
        assertNotNull(comments);
        assertEquals(template, comments.getTemplate());
        assertEquals(client, comments.getUser());

    }

    @Test
    public void testSensationsGettersAndSetters() {

        // Test setters
        comments.setId(3L);
        comments.setText("newComment");
        comments.setCommentDate(LocalDateTime.of(2002, 1, 1, 0, 0));
        comments.setTemplate(newTemplate);
        comments.setUser(newClient);

        // Test getters
        assertEquals(3L, comments.getId().longValue());
        assertEquals("newComment", comments.getText());
        assertEquals(LocalDateTime.of(2002, 1, 1, 0, 0), comments.getCommentDate());
        assertEquals(newTemplate, comments.getTemplate());
        assertEquals(newClient, comments.getUser());

    }

}
