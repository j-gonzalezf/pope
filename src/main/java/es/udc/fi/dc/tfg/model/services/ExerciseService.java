package es.udc.fi.dc.tfg.model.services;

import es.udc.fi.dc.tfg.model.entities.Exercises;
import java.util.List;

/**
 * Interfaz ExerciseService.
 */
public interface ExerciseService {

    /**
     * Crea un nuevo ejercicio.
     *
     * @param exercise El objeto Exercises que representa el ejercicio a crear.
     */
    void addExercise(Exercises exercise);

    /**
     * Devuelve una lista con los ejercicios añadidos por un entrenador.
     *
     * @param trainerId El ID del entrenador.
     * @return La lista de objetos Exercises que representa los ejercicios.
     */
    List<Exercises> getExercises(Long trainerId);

}
