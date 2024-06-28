package es.udc.fi.dc.tfg.model.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import es.udc.fi.dc.tfg.model.common.exceptions.DuplicateInstanceException;
import es.udc.fi.dc.tfg.model.common.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.tfg.model.entities.Exercises;
import es.udc.fi.dc.tfg.model.entities.Users;
import jakarta.transaction.Transactional;
import java.util.List;

/**
 * Clase ExerciseServiceTest.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ExerciseServiceTest {

    private final Long NON_EXISTENT_ID = Long.valueOf(-1);

    /**
     * El user service.
     */
    @Autowired
    private UserService userService;

    /**
     * El exercise service.
     */
    @Autowired
    private ExerciseService exerciseService;

    /**
     * Crea un ejercicio.
     *
     * @return El objeto Exercises que representa al ejercicio
     */
    private Exercises addExercise() {
        return new Exercises("exerciseName", "description", "exerciseType",
                null, null, null, null);
    }

    /**
     * Test para crear ejercicios.
     *
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email.
     */
    @Test
    public void testAddExercise() throws DuplicateInstanceException {

        Exercises exercise = addExercise();
        Users trainer = new Users("t@t.com", "password1", "fullName1", "987654321", "", "");

        userService.signUp(trainer);

        exercise.setTrainer(trainer);

        exerciseService.addExercise(exercise);

    }

    /**
     * Test para obtener los ejercicios añadidos por un entrenador.
     *
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email.
     */
    @Test
    public void testGetExercises() throws DuplicateInstanceException {

        Exercises exercise1 = addExercise();
        Exercises exercise2 = addExercise();
        Users trainer = new Users("t@t.com", "password1", "fullName1", "987654321", "", "");

        userService.signUp(trainer);

        exercise1.setTrainer(trainer);
        exerciseService.addExercise(exercise1);

        exercise2.setTrainer(trainer);
        exerciseService.addExercise(exercise2);

        List<Exercises> exercises = exerciseService.getExercises(trainer.getId());

        assertEquals(2, exercises.size());
        assertTrue(exercises.contains(exercise1));
        assertTrue(exercises.contains(exercise2));

    }

    /**
     * Test para editar ejercicios.
     *
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email.
     * @throws InstanceNotFoundException si no se encuentra ningún ejercicio.
     */
    @Test
    public void testUpdateExercise()
            throws DuplicateInstanceException, InstanceNotFoundException {

        Exercises exercise = addExercise();
        Users trainer = new Users("t@t.com", "password1", "fullName1", "987654321", "", "");

        userService.signUp(trainer);

        exercise.setTrainer(trainer);
        exerciseService.addExercise(exercise);

        Exercises updatedExercise = exerciseService.updateExercise(
                exercise.getId(), "new Name", "new Description", "new exerciseType",
                null, null, null);

        assertEquals("new Name", updatedExercise.getName());
        assertEquals("new Description", updatedExercise.getDescription());
        assertEquals("new exerciseType", updatedExercise.getType());

    }

    /**
     * Test para actualizar un ejercicio con un ID inexistente.
     *
     * @throws InstanceNotFoundException si no se encuentra un ejercicio con el
     * ID proporcionado.
     */
    @Test
    public void testUpdateCycleWithNonExistentId() throws InstanceNotFoundException {
        assertThrows(InstanceNotFoundException.class,
                () -> exerciseService.updateExercise(NON_EXISTENT_ID, "new Name",
                        "new Description", "new exerciseType",
                        null, null, null));
    }

}
