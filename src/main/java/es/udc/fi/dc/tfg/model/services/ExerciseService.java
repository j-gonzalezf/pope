package es.udc.fi.dc.tfg.model.services;

import es.udc.fi.dc.tfg.model.entities.Exercises;

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
    
}
