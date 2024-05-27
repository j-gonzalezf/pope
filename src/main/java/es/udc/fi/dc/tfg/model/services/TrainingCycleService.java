package es.udc.fi.dc.tfg.model.services;

import es.udc.fi.dc.tfg.model.common.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.tfg.model.entities.TrainingCycles;
import java.util.List;

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
    
    /**
     * Devuelve una lista con los ciclos del cliente de un entrenador.
     *
     * @param trainerId El ID del entrenador.
     * @param clientId El ID del cliente.
     * @return La lista de objetos TrainingCycles que representa los ciclos.
     * @throws InstanceNotFoundException si no se encuentra ningún ciclo.
     */
    List<TrainingCycles> getCycles(Long trainerId, Long clientId) 
            throws InstanceNotFoundException;

}
