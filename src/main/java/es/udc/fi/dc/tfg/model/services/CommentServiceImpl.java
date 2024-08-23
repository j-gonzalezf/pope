package es.udc.fi.dc.tfg.model.services;

import es.udc.fi.dc.tfg.model.entities.CommentDao;
import es.udc.fi.dc.tfg.model.entities.Comments;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Clase CommentServiceImpl.
 */
@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    /**
     * El comment dao.
     */
    @Autowired
    private CommentDao commentDao;

    /**
     * Guarda un nuevo comentario.
     *
     * @param comment el objeto Comments que representa el comentario.
     */
    @Override
    public void writeComment(Comments comment) {

        commentDao.save(comment);

    }

    /**
     * Devuelve los comentarios de una plantilla a partir del ID de su plantilla
     *
     * @param templateId El ID de la plantilla.
     * @return La lista de objetos Comments que representa los comentarios.
     */
    @Override
    public List<Comments> getTemplateComments(Long templateId) {

        List<Comments> comments = commentDao.findByTemplateId(templateId);
        return comments;

    }

}
