package es.udc.fi.dc.tfg.model.entities;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    /**
     * Devuelve una lista de registros de sensaciones a partir del id de un
     * cliente.
     *
     * @param clientId El ID del cliente.
     * @return La lista de objetos Sensations que representa las sensaciones.
     */
    @Query("SELECT s FROM Sensations s WHERE s.client.id = :clientId ORDER BY s.sensationDate DESC")
    List<Sensations> findByClientId(@Param("clientId") Long clientId);

}
