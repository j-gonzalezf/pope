package es.udc.fi.dc.tfg.model.services;

import es.udc.fi.dc.tfg.model.entities.TrainingCycles;

/**
 * Interfaz TrainingCycleService.
 */
public interface TrainingCycleService {

    /**
     * Crea un nuevo ciclo de entrenamiento.
     *
     * @param cycle El objeto TrainingCycles que representa el ciclo a crear.
     */
    void createCycle(TrainingCycles cycle);

}
