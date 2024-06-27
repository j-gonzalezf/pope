package es.udc.fi.dc.tfg.model.entities;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interfaz ExerciseDao
 */
public interface ExerciseDao extends JpaRepository<Exercises, Long> {
    
    /**
     * Devuelve una lista con los ejercicios añadidos por un entrenador.
     * 
     * @param trainerId El ID del entrenador.
     * @return La lista de objetos Exercises que representa los ejercicios.
     */
    List<Exercises> findByTrainerIdOrderByNameAsc(Long trainerId);
    
}
