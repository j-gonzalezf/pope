package es.udc.fi.dc.tfg.model.entities;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interfaz CommentDao
 */
public interface CommentDao extends JpaRepository<Comments, Long> {

    /**
     * Devuelve una lista de comentarios a partir del ID de su plantilla.
     *
     * @param templateId El ID de la plantilla.
     * @return La lista de objetos Comments que representa los comentarios.
     */
    List<Comments> findByTemplateId(Long templateId);

}
