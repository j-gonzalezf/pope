package es.udc.fi.dc.tfg.model.services;

import es.udc.fi.dc.tfg.model.entities.ExerciseDao;
import es.udc.fi.dc.tfg.model.entities.Exercises;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Clase TrainingCycleServiceImpl.
 */
@Service
@Transactional
public class ExerciseServiceImpl implements ExerciseService {
    
    /**
     * El exercise dao.
     */
    @Autowired
    private ExerciseDao exerciseDao;
    
    /**
     * Crea un nuevo ejercicio
     *
     * @param exercise El objeto Exercises que representa el ejercicio a crear.
     */
    @Override
    public void addExercise(Exercises exercise) {
        exerciseDao.save(exercise);
    }
    
}
