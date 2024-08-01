package es.udc.fi.dc.tfg.model.services;

import es.udc.fi.dc.tfg.model.entities.Weights;

/**
 * Interfaz UserExtraService.
 */
public interface UserExtraService {

    /**
     * Crea un nuevo registro de peso.
     *
     * @param weight el objeto Weights que representa el peso a registrar.
     */
    void weightRegister(Weights weight);

    /**
     * Devuelve el último peso registrado de un cliente.
     *
     * @param clientId El ID del cliente.
     * @return El objeto Weights que representa el peso.
     */
    Weights getLastWeight(Long clientId);

}
