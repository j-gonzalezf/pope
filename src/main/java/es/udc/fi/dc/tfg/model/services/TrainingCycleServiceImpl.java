package es.udc.fi.dc.tfg.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.tfg.model.common.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.tfg.model.entities.TrainingCycleDao;
import es.udc.fi.dc.tfg.model.entities.TrainingCycles;
import java.util.List;

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
     * @throws InstanceNotFoundException si no se encuentra ningún ciclo.
     */
    @Override
    public List<TrainingCycles> getCycles(Long trainerId, Long clientId)
            throws InstanceNotFoundException {

        List<TrainingCycles> cycles = cycleDao.findCycles(trainerId, clientId);
        return cycles;

    }

}
