package es.udc.fi.dc.tfg.model.services;

import es.udc.fi.dc.tfg.model.common.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.tfg.model.entities.Exercises;
import es.udc.fi.dc.tfg.model.entities.TemplateRows;
import es.udc.fi.dc.tfg.model.entities.Templates;
import java.math.BigDecimal;
import java.util.List;

/**
 * Interfaz TemplateService.
 */
public interface TemplateService {

    /**
     * Crea una nueva plantilla de entrenamiento
     *
     * @param template El objeto Templates que representa la plantilla a crear.
     */
    void createTemplate(Templates template);

    /**
     * Añade una nueva fila a una plantilla de entrenamiento
     *
     * @param templateRow El objeto TemplateRows que representa la fila a añadir
     * a la plantilla.
     */
    void addTemplateRow(TemplateRows templateRow);

    /**
     * Devuelve una lista con las plantillas de un ciclo.
     *
     * @param cycleId El ID del ciclo.
     * @return La lista de objetos Templlates que representa las plantillas.
     */
    List<Templates> getTemplates(Long cycleId);

    /**
     * Devuelve una lista con las filas de una plantilla.
     *
     * @param templateId El ID de la plantilla.
     * @return La lista de objetos TemplateRows que representa las filas.
     */
    List<TemplateRows> getTemplateRows(Long templateId);

    /**
     * Devuelve una plantilla a partir de su ID.
     *
     * @param templateId El ID de la plantilla.
     * @return El objeto Templates que representa la plantilla solicitada.
     * @throws InstanceNotFoundException si no se encuentra ninguna plantilla.
     */
    Templates getTemplateInfo(Long templateId) throws InstanceNotFoundException;

    /**
     * Edita una plantilla.
     *
     * @param id El ID de la plantilla.
     * @param name El nombre de la plantilla.
     * @return El objeto Templates que representa la plantilla actualizada.
     * @throws InstanceNotFoundException si no se encuentra ninguna plantilla.
     */
    Templates updateTemplate(Long id, String name) throws InstanceNotFoundException;

    /**
     * Edita la fila de una plantilla.
     *
     * @param id El ID de la fila.
     * @param series Las series a realizar del ejercicio.
     * @param reps Las repeticiones a realizar del ejercicio.
     * @param weight El peso a levantar en el ejercicio.
     * @param exercise El ejercicio a realizar.
     * @return El objeto TemplateRows que representa la fila actualizada.
     * @throws InstanceNotFoundException si no se encuentra ninguna fila.
     */
    TemplateRows updateTemplateRow(Long id, Integer series, Integer reps,
            BigDecimal weight, Exercises exercise) throws InstanceNotFoundException;

    /**
     * Elimina una plantilla.
     *
     * @param id El ID de la plantilla.
     * @return El ID de la plantilla que ha sido eliminada.
     * @throws InstanceNotFoundException si no se encuentra una plantilla con el
     * ID proporcionado.
     */
    Long deleteTemplate(Long id) throws InstanceNotFoundException;

    /**
     * Elimina la fila de una plantilla.
     *
     * @param id El ID de la fila.
     * @return El ID de la fila que ha sido eliminada.
     * @throws InstanceNotFoundException si no se encuentra una fila con el ID
     * proporcionado.
     */
    Long deleteTemplateRow(Long id) throws InstanceNotFoundException;

}
