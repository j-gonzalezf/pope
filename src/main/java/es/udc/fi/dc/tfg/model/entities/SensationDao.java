package es.udc.fi.dc.tfg.model.entities;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interfaz SensationDao
 */
public interface SensationDao extends JpaRepository<Sensations, Long> {

    /**
     * Devuelve un registro de sensaciones a partir del id de su plantilla.
     *
     * @param templateId El ID de la plantilla.
     * @return El objeto Sensations que representa las sensaciones.
     */
    Optional<Sensations> findByTemplateId(Long templateId);

}
