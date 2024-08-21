package es.udc.fi.dc.tfg.model.entities;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Interfaz WeightDao
 */
public interface WeightDao extends JpaRepository<Weights, Long> {

    /**
     * Devuelve el último peso regitrado de un cliente.
     *
     * @param clientId El ID del cliente.
     * @return El objeto Weights que representa el peso.
     */
    @Query("SELECT w FROM Weights w WHERE w.client.id = :clientId ORDER BY w.weightDate DESC")
    List<Weights> findByClientId(@Param("clientId") Long clientId);

}
