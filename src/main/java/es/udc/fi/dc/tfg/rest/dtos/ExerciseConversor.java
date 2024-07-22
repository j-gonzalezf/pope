package es.udc.fi.dc.tfg.rest.dtos;

import es.udc.fi.dc.tfg.model.entities.Exercises;
import es.udc.fi.dc.tfg.model.entities.Users;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase ExerciseConversor.
 */
public class ExerciseConversor {

    /**
     * Instancia un nuevo exercise conversor
     */
    private ExerciseConversor() {
    }

    /**
     * Convierte un objeto Exercises a un objeto ExerciseDto
     *
     * @param exercise el objeto Exercises a convertir
     * @return un nuevo objeto ExerciseDto que contiene los mismos datos que el
     * objeto Exercises proporcionado
     */
    public static final ExerciseDto toExerciseDto(Exercises exercise) {
        return new ExerciseDto(exercise.getId(), exercise.getName(),
                exercise.getDescription(), exercise.getType(), exercise.getBodyPart(),
                exercise.getEquipment(), exercise.getLink(), exercise.getTrainer().getId());
    }

    /**
     * Convierte una lista de objetos Exercises a objetos ExerciseDto.
     *
     * @param exercises la lista de objetos Exercises a convertir
     * @return una nueva lista de objetos ExerciseDto que contiene los mismos
     * datos que la lista de objetos Exercises proporcionada
     */
    public static final List<ExerciseDto> toExercisesDto(List<Exercises> exercises) {
        return exercises.stream().map(exercise -> {
            return toExerciseDto(exercise);
        }).collect(Collectors.toList());
    }

    /**
     * Convierte un objeto ExerciseDto a un objeto Exercises
     *
     * @param exerciseDto el objeto ExerciseDto a convertir
     * @param trainer el entrenador del ExerciseDto
     * @return un nuevo objeto Exercises que contiene los mismos datos que el
     * objeto ExerciseDto proporcionado
     */
    public static final Exercises toExercises(ExerciseDto exerciseDto, Users trainer) {
        return new Exercises(exerciseDto.getName(), exerciseDto.getDescription(),
                exerciseDto.getType(), exerciseDto.getBodyPart(),
                exerciseDto.getEquipment(), exerciseDto.getLink(), trainer);
    }

}
