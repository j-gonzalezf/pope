package es.udc.fi.dc.tfg.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.tfg.model.entities.TrainingCycleDao;
import es.udc.fi.dc.tfg.model.entities.TrainingCycles;

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

}
