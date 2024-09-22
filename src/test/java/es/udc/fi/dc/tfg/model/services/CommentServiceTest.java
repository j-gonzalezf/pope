package es.udc.fi.dc.tfg.model.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.tfg.model.common.exceptions.DuplicateInstanceException;
import es.udc.fi.dc.tfg.model.common.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.tfg.model.entities.Comments;
import es.udc.fi.dc.tfg.model.entities.Templates;
import es.udc.fi.dc.tfg.model.entities.TrainingCycles;
import es.udc.fi.dc.tfg.model.entities.Users;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Clase CommentServiceTest.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CommentServiceTest {

    /**
     * El user service.
     */
    @Autowired
    private UserService userService;

    /**
     * El training cycle service.
     */
    @Autowired
    private TrainingCycleService cycleService;

    /**
     * El template service.
     */
    @Autowired
    private TemplateService templateService;

    /**
     * El comment service.
     */
    @Autowired
    private CommentService commentService;

    /**
     * Crea un comentario.
     *
     * @return El objeto Comments que representa el comentario
     */
    private Comments writeComment() {
        return new Comments("comment", LocalDateTime.of(2001, 1, 1, 0, 0), null, null);
    }

    /**
     * Test para guardar comentarios.
     *
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email.
     * @throws InstanceNotFoundException si no se encuentra ninguna plantilla.
     */
    @Test
    public void testWriteComment()
            throws DuplicateInstanceException, InstanceNotFoundException {

        Comments comment = writeComment();
        Users trainer = new Users("t@t.com", "password1", "fullName1", "987654321", "", "");
        Users client = new Users("c@c.com", "password2", "fullName2", "123456789", "",
                LocalDate.of(2000, 1, 1), "No", "Ninguno", new BigDecimal("170"), trainer);

        userService.signUp(trainer);
        userService.signUp(client);

        TrainingCycles cycle = new TrainingCycles("cycleName", "description",
                LocalDate.of(2000, 1, 1), LocalDate.of(2001, 1, 1), trainer, client);

        cycleService.createCycle(cycle);

        Templates template = new Templates("templateName",
                LocalDateTime.of(2000, 1, 1, 0, 0), cycle);

        templateService.createTemplate(template);

        comment.setTemplate(template);
        comment.setUser(client);

        commentService.writeComment(comment);

    }

    /**
     * Test para obtener los comentarios de una plantilla.
     *
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email.
     */
    @Test
    public void testGetSensations() throws DuplicateInstanceException {

        Comments comment1 = writeComment();
        Comments comment2 = writeComment();
        Users trainer = new Users("t@t.com", "password1", "fullName1", "987654321", "", "");
        Users client = new Users("c@c.com", "password2", "fullName2", "123456789", "",
                LocalDate.of(2000, 1, 1), "No", "Ninguno", new BigDecimal("170"), trainer);

        userService.signUp(trainer);
        userService.signUp(client);

        TrainingCycles cycle = new TrainingCycles("cycleName", "description",
                LocalDate.of(2000, 1, 1), LocalDate.of(2001, 1, 1), trainer, client);

        cycleService.createCycle(cycle);

        Templates template = new Templates("templateName1",
                LocalDateTime.of(2000, 1, 1, 0, 0), cycle);

        templateService.createTemplate(template);

        comment1.setTemplate(template);
        comment1.setUser(trainer);

        comment2.setTemplate(template);
        comment2.setUser(client);

        commentService.writeComment(comment1);
        commentService.writeComment(comment2);

        List<Comments> comments = commentService.getTemplateComments(template.getId());

        assertEquals(2, comments.size());
        assertTrue(comments.contains(comment1));
        assertTrue(comments.contains(comment2));

    }

}
