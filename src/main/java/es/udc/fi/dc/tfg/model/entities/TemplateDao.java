package es.udc.fi.dc.tfg.model.entities;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Interfaz TemplateDao
 */
public interface TemplateDao extends JpaRepository<Templates, Long> {

    /**
     * Devuelve una lista con las plantillas de un ciclo.
     *
     * @param cycleId El ID del ciclo.
     * @return La lista de objetos Templates que representa las plantillas.
     */
    @Query("SELECT t FROM Templates t WHERE t.cycle.id = :cycleId ORDER BY t.creationDate DESC")
    List<Templates> findTemplates(@Param("cycleId") Long cycleId);

}
