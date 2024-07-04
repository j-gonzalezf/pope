package es.udc.fi.dc.tfg.model.services;

import es.udc.fi.dc.tfg.model.common.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.tfg.model.entities.TemplateRows;
import es.udc.fi.dc.tfg.model.entities.Templates;

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
     * Devuelve una plantilla a partir de su ID.
     *
     * @param templateId El ID de la plantilla.
     * @return El objeto Templates que representa la plantilla solicitada.
     * @throws InstanceNotFoundException si no se encuentra ninguna plantilla.
     */
    Templates getTemplateInfo(Long templateId) throws InstanceNotFoundException;

}
