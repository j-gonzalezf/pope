package es.udc.fi.dc.tfg.model.services;

import es.udc.fi.dc.tfg.model.entities.Comments;

/**
 * Interfaz CommentService.
 */
public interface CommentService {

    /**
     * Guarda un nuevo comentario.
     *
     * @param comment el objeto Comments que representa el comentario.
     */
    void writeComment(Comments comment);

}
