package es.udc.fi.dc.tfg.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.tfg.model.common.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.tfg.model.entities.ExerciseDao;
import es.udc.fi.dc.tfg.model.entities.Exercises;
import java.util.List;
import java.util.Optional;

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
     * Crea un nuevo ejercicio.
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

        List<Exercises> exercises = exerciseDao.findByTrainerIdOrderByNameAsc(trainerId);
        return exercises;

    }

    /**
     * Edita un ejercicio.
     *
     * @param id El ID del ejercicio.
     * @param name El nombre del ejercicio.
     * @param description La descripción del ejercicio.
     * @param type La categoría del ejercicio.
     * @param bodyPart La parte del cuerpo que se trabaja con el ejercicio.
     * @param equipment El equipamiento necesario para realizar el ejercicio.
     * @param link El enlace de referencia para realizar bien el ejercicio.
     * @return El objeto Exercises que representa el ejercicio actualizado.
     * @throws InstanceNotFoundException si no se encuentra ningún ejercicio.
     */
    @Override
    public Exercises updateExercise(Long id, String name, String description,
            String type, String bodyPart, String equipment, String link)
            throws InstanceNotFoundException {

        Optional<Exercises> Oexercise = exerciseDao.findById(id);

        if (!Oexercise.isPresent()) {
            throw new InstanceNotFoundException("project.entities.trainingCycles", id);
        }

        Exercises exercise = Oexercise.get();

        exercise.setName(name);
        exercise.setDescription(description);
        exercise.setType(type);
        exercise.setBodyPart(bodyPart);
        exercise.setEquipment(equipment);
        exercise.setLink(link);

        return exercise;

    }

}
