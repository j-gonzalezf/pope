package es.udc.fi.dc.tfg.rest.controllers;

import static es.udc.fi.dc.tfg.rest.dtos.ExerciseConversor.toExerciseDto;
import static es.udc.fi.dc.tfg.rest.dtos.ExerciseConversor.toExercisesDto;
import static es.udc.fi.dc.tfg.rest.dtos.ExerciseConversor.toExercises;
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.udc.fi.dc.tfg.model.common.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.tfg.model.entities.Exercises;
import es.udc.fi.dc.tfg.model.entities.Users;
import es.udc.fi.dc.tfg.model.services.ExerciseService;
import es.udc.fi.dc.tfg.model.services.UserService;
import es.udc.fi.dc.tfg.model.services.exceptions.PermissionException;
import es.udc.fi.dc.tfg.rest.dtos.ExerciseDto;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Clase ExerciseController.
 */
@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private UserService userService;

    // Método para validar un usuario, userId siempre es un TRAINER
    private void validateUser(Long userId, Users trainer) throws PermissionException {
        try {
            if (!trainer.getId().equals(userId)) {
                throw new PermissionException();
            }
        } catch (NullPointerException e) {
            throw new PermissionException();
        }
    }

    /**
     * Añadir un nuevo ejercicio.
     *
     * @param userId el ID del usuario que realiza la petición
     * @param exerciseDto el DTO del ejercicio
     * @return una ResponseEntity que contiene un ExerciseDto
     * @throws InstanceNotFoundException si no se encuentra un usuario con el ID
     * proporcionado
     * @throws PermissionException si el ID del usuario que realiza la petición
     * no coincide con el ID del entrenador que añade el ejercicio
     */
    @PostMapping("/exercise/add")
    public ResponseEntity<ExerciseDto> addExercise(@RequestAttribute Long userId,
            @Validated({ExerciseDto.AllValidations.class}) @RequestBody ExerciseDto exerciseDto)
            throws InstanceNotFoundException, PermissionException {

        Users trainer = userService.loginFromId(exerciseDto.getTrainerId());

        validateUser(userId, trainer);

        Exercises exercise = toExercises(exerciseDto, trainer);

        exerciseService.addExercise(exercise);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(exercise.getId()).toUri();

        return ResponseEntity.created(location).body(toExerciseDto(exercise));

    }

    /**
     * Devuelve la lista de ejercicios añadidos por un entrenador.
     *
     * @param userId el ID del usuario que realiza la petición
     * @param trainerId el ID del entrenador
     * @return una lista de ejercicios
     * @throws PermissionException si el ID del usuario que realiza la petición
     * no coincide con el ID del entrenador
     * @throws InstanceNotFoundException si no se encuentra el entrenador
     */
    @GetMapping("/{trainerId}")
    public List<ExerciseDto> getTrainingCycles(@RequestAttribute Long userId,
            @PathVariable("trainerId") Long trainerId)
            throws PermissionException, InstanceNotFoundException {

        Users trainer = userService.loginFromId(trainerId);

        validateUser(userId, trainer);

        List<Exercises> exercises = exerciseService.getExercises(trainerId);

        return toExercisesDto(exercises);

    }

}
