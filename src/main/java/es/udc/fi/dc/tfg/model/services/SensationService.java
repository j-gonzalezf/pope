package es.udc.fi.dc.tfg.model.services;

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

}
