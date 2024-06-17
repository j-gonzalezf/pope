package es.udc.fi.dc.tfg.model.services;

import es.udc.fi.dc.tfg.model.common.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.tfg.model.entities.TrainingCycles;
import java.time.LocalDate;
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
     */
    List<TrainingCycles> getCycles(Long trainerId, Long clientId);

    /**
     * Devuelve un ciclo a partir de su ID.
     *
     * @param cycleId El ID del ciclo.
     * @return El objeto TrainingCycles que representa el ciclo solicitado.
     * @throws InstanceNotFoundException si no se encuentra ningún ciclo.
     */
    public TrainingCycles getCycleInfo(Long cycleId) throws InstanceNotFoundException;

    /**
     * Edita un ciclo de entrenamiento.
     *
     * @param id El ID del ciclo.
     * @param name El nombre del ciclo.
     * @param description La descripción del ciclo.
     * @param fromDate La fecha de inicio del ciclo.
     * @param toDate La fecha de fin del ciclo.
     * @return El objeto TrainingCycles que representa el ciclo actualizado.
     * @throws InstanceNotFoundException si no se encuentra ningún ciclo.
     */
    TrainingCycles updateCycle(Long id, String name, String description,
            LocalDate fromDate, LocalDate toDate) throws InstanceNotFoundException;

}
