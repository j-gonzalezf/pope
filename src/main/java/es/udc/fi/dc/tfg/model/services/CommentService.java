package es.udc.fi.dc.tfg.model.services;

import es.udc.fi.dc.tfg.model.entities.Comments;
import java.util.List;

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

    /**
     * Devuelve los comentarios de una plantilla a partir del ID de su plantilla
     *
     * @param templateId El ID de la plantilla.
     * @return La lista de objetos Comments que representa los comentarios.
     */
    List<Comments> getTemplateComments(Long templateId);

}
