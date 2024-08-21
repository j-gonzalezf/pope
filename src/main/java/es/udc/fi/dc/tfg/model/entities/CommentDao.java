package es.udc.fi.dc.tfg.model.entities;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interfaz CommentDao
 */
public interface CommentDao extends JpaRepository<Comments, Long> {
}
