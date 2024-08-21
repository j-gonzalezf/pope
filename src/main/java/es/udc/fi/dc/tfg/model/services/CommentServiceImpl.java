package es.udc.fi.dc.tfg.model.services;

import es.udc.fi.dc.tfg.model.entities.CommentDao;
import es.udc.fi.dc.tfg.model.entities.Comments;
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

}
