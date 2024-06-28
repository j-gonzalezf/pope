package es.udc.fi.dc.tfg.model.services;

import es.udc.fi.dc.tfg.model.common.exceptions.InstanceNotFoundException;
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
    Exercises updateExercise(Long id, String name, String description,
            String type, String bodyPart, String equipment, String link)
            throws InstanceNotFoundException;

}
