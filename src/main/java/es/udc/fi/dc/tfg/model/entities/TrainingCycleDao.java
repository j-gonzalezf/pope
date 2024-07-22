package es.udc.fi.dc.tfg.model.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Interfaz TrainingCycleDao.
 */
public interface TrainingCycleDao extends JpaRepository<TrainingCycles, Long> {

    /**
     * Devuelve una lista con los ciclos del cliente de un entrenador.
     *
     * @param trainerId El ID del entrenador.
     * @param clientId El ID del cliente.
     * @return La lista de objetos TrainingCycles que representa los ciclos.
     */
    @Query("SELECT tc FROM TrainingCycles tc WHERE tc.trainer.id = :trainerId AND tc.client.id = :clientId ORDER BY tc.fromDate DESC")
    List<TrainingCycles> findCycles(@Param("trainerId") Long trainerId, @Param("clientId") Long clientId);

}
