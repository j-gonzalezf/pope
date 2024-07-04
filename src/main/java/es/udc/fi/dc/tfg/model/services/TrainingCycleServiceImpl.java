package es.udc.fi.dc.tfg.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.tfg.model.common.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.tfg.model.entities.TrainingCycleDao;
import es.udc.fi.dc.tfg.model.entities.TrainingCycles;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Clase TrainingCycleServiceImpl.
 */
@Service
@Transactional
public class TrainingCycleServiceImpl implements TrainingCycleService {

    /**
     * El training cycle dao.
     */
    @Autowired
    private TrainingCycleDao cycleDao;

    /**
     * Crea un nuevo ciclo de entrenamiento
     *
     * @param cycle El objeto TrainingCycles que representa el ciclo a crear.
     */
    @Override
    public void createCycle(TrainingCycles cycle) {
        cycleDao.save(cycle);
    }

    /**
     * Devuelve una lista con los ciclos del cliente de un entrenador.
     *
     * @param trainerId El ID del entrenador.
     * @param clientId El ID del cliente.
     * @return La lista de objetos TrainingCycles que representa los ciclos.
     */
    @Override
    public List<TrainingCycles> getCycles(Long trainerId, Long clientId) {

        List<TrainingCycles> cycles = cycleDao.findCycles(trainerId, clientId);
        return cycles;

    }

    /**
     * Devuelve un ciclo a partir de su ID.
     *
     * @param cycleId El ID del ciclo.
     * @return El objeto TrainingCycles que representa el ciclo solicitado.
     * @throws InstanceNotFoundException si no se encuentra ningún ciclo.
     */
    @Override
    public TrainingCycles getCycleInfo(Long cycleId) throws InstanceNotFoundException {

        Optional<TrainingCycles> Ocycle = cycleDao.findById(cycleId);

        if (!Ocycle.isPresent()) {
            throw new InstanceNotFoundException("project.entities.trainingCycles", cycleId);
        }

        TrainingCycles cycle = Ocycle.get();

        return cycle;

    }

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
    @Override
    public TrainingCycles updateCycle(Long id, String name, String description,
            LocalDate fromDate, LocalDate toDate) throws InstanceNotFoundException {

        Optional<TrainingCycles> Ocycle = cycleDao.findById(id);

        if (!Ocycle.isPresent()) {
            throw new InstanceNotFoundException("project.entities.trainingCycles", id);
        }

        TrainingCycles cycle = Ocycle.get();

        cycle.setName(name);
        cycle.setDescription(description);
        cycle.setFromDate(fromDate);
        cycle.setToDate(toDate);

        return cycle;

    }

    /**
     * Elimina un ciclo de entrenamiento.
     *
     * @param id El ID del ciclo.
     * @return El ID del ciclo que ha sido eliminado.
     * @throws InstanceNotFoundException si no se encuentra un ciclo con el ID
     * proporcionado.
     */
    @Override
    public Long deleteCycle(Long id) throws InstanceNotFoundException {

        Optional<TrainingCycles> Ocycle = cycleDao.findById(id);

        if (!Ocycle.isPresent()) {
            throw new InstanceNotFoundException("project.entities.trainingCycles", id);
        }

        cycleDao.deleteById(id);

        return id;

    }

}
