package es.udc.fi.dc.tfg.model.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ExercisesTest {

    private Exercises voidExercise;
    private Exercises exercise;
    private Users trainer;
    private Users newTrainer;

    @Before
    public void setUp() {
        voidExercise = new Exercises();
        trainer = new Users("trainer@example.com", "password", "Test Trainer",
                "1234567890", null, null);
        newTrainer = new Users("newTrainer@example.com", "password", "Test Trainer",
                "1234567890", null, null);
        exercise = new Exercises("exerciseName", "exerciseDescription",
                "exerciseType", null, null, null, trainer);
    }

    @Test
    public void testExerciseConstructors() {

        // Test voidExercise constructor
        assertNotNull(voidExercise);
        assertNull(voidExercise.getId());

        // Test exercise constructor
        assertNotNull(exercise);
        assertEquals("exerciseName", exercise.getName());
        assertEquals("exerciseDescription", exercise.getDescription());
        assertEquals("exerciseType", exercise.getType());
        assertEquals(trainer, exercise.getTrainer());

    }

    @Test
    public void testExerciseGettersAndSetters() {

        // Test setters
        exercise.setId(3L);
        exercise.setName("newExerciseName");
        exercise.setDescription("newExerciseDescription");
        exercise.setType("newExerciseType");
        exercise.setBodyPart("newBodyPart");
        exercise.setEquipment("newEquipment");
        exercise.setLink("https://linktr.ee/");
        exercise.setTrainer(newTrainer);

        // Test getters
        assertEquals(3L, exercise.getId().longValue());
        assertEquals("newExerciseName", exercise.getName());
        assertEquals("newExerciseDescription", exercise.getDescription());
        assertEquals("newExerciseType", exercise.getType());
        assertEquals("newBodyPart", exercise.getBodyPart());
        assertEquals("newEquipment", exercise.getEquipment());
        assertEquals("https://linktr.ee/", exercise.getLink());
        assertEquals(newTrainer, exercise.getTrainer());

    }

}
