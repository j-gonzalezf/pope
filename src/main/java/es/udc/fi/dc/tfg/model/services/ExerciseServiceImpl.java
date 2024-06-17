package es.udc.fi.dc.tfg.model.services;

import es.udc.fi.dc.tfg.model.common.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.tfg.model.entities.ExerciseDao;
import es.udc.fi.dc.tfg.model.entities.Exercises;
import java.util.List;
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

    /**
     * Devuelve una lista con los ejercicios añadidos por un entrenador.
     *
     * @param trainerId El ID del entrenador.
     * @return La lista de objetos Exercises que representa los ejercicios.
     */
    @Override
    public List<Exercises> getExercises(Long trainerId) {

        List<Exercises> exercises = exerciseDao.findByTrainerId(trainerId);
        return exercises;

    }

}
