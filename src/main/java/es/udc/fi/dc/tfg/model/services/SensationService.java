package es.udc.fi.dc.tfg.model.services;

import es.udc.fi.dc.tfg.model.common.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.tfg.model.entities.Sensations;

/**
 * Interfaz SensationService.
 */
public interface SensationService {

    /**
     * Crea un nuevo registro de sensaciones.
     *
     * @param sensations el objeto Sensations que representa las sensaciones a
     * registrar.
     */
    void sensationsRegister(Sensations sensations);

    /**
     * Devuelve un registro de sensaciones a partir del ID de su plantilla
     *
     * @param templateId El ID de la plantilla.
     * @return El objeto Sensations que representa el registro solicitado.
     * @throws InstanceNotFoundException si no se encuentra ninguna plantilla.
     */
    Sensations getSensationInfo(Long templateId) throws InstanceNotFoundException;

    /**
     * Edita un registro de sensaciones.
     *
     * @param id El ID del registro.
     * @param fatigue La fatiga registrada.
     * @param stiffness Las agujetas registradas.
     * @param motivation La motivación registrada.
     * @param sleep La calidad de sueño registrada.
     * @return El objeto Sensations que representa el registro actualizado.
     * @throws InstanceNotFoundException si no se encuentra ningún registro.
     */
    Sensations updateSensations(Long id, Integer fatigue, Integer stiffness,
            Integer motivation, Integer sleep) throws InstanceNotFoundException;

}
