package es.udc.fi.dc.tfg.model.entities;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interfaz TemplateRowDao
 */
public interface TemplateRowDao extends JpaRepository<TemplateRows, Long> {

    /**
     * Devuelve una lista con las filas de una plantilla.
     *
     * @param templateId El ID de la plantilla.
     * @return La lista de objetos TemplateRows que representa las filas.
     */
    List<TemplateRows> findByTemplateId(Long templateId);

}
