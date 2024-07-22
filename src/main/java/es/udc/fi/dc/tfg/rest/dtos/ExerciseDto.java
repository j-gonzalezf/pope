package es.udc.fi.dc.tfg.rest.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

/**
 * Clase ExerciseDto.
 */
public class ExerciseDto {

    /**
     * La interfaz AllValidations.
     */
    public interface AllValidations {
    }

    /**
     * La interfaz UpdateValidations.
     */
    public interface UpdateValidations {
    }

    /**
     * Identificador del ejercicio.
     */
    private Long id;

    /**
     * Nombre del ejercicio.
     */
    private String name;

    /**
     * Descripción del ejercicio.
     */
    private String description;

    /**
     * Categoría del ejercicio.
     */
    private String type;

    /**
     * Parte del cuerpo que se trabaja con el ejercicio, puede ser nulo.
     */
    private String bodyPart;

    /**
     * Equipamiento necesario para realizar el ejercicio, puede ser nulo.
     */
    private String equipment;

    /**
     * URL de referencia para la correcta realización del ejercicio, puede ser
     * nulo.
     */
    private String link;

    /**
     * Identificador del entrenador creador del ejercicio.
     */
    private Long trainerId;

    /**
     * Instantiates a new exercise dto.
     */
    public ExerciseDto() {
    }

    /**
     * Constructor de la clase ExerciseDto.
     *
     * @param id El identificador del ejercicio.
     * @param name El nombre del ejercicio.
     * @param description La descripción del ejercicio.
     * @param type La categoría del ejercicio.
     * @param bodyPart La parte del cuerpo que se trabaja con el ejercicio.
     * @param equipment El equipamiento necesario para realizar el ejercicio.
     * @param link La URL de referencia para la correcta realización del
     * ejercicio.
     * @param trainerId El ID del entrenador creador del ejercicio.
     */
    public ExerciseDto(Long id, String name, String description, String type,
            String bodyPart, String equipment, String link, Long trainerId) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.bodyPart = bodyPart;
        this.equipment = equipment;
        this.link = link;
        this.trainerId = trainerId;

    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the exercise name.
     *
     * @return the exercise name
     */
    @NotNull(groups = {AllValidations.class, UpdateValidations.class})
    @Size(min = 1, max = 255, groups = {AllValidations.class, UpdateValidations.class})
    public String getName() {
        return name;
    }

    /**
     * Sets the exercise name.
     *
     * @param name the new exercise name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    @NotNull(groups = {AllValidations.class, UpdateValidations.class})
    @Size(max = 500, groups = {AllValidations.class, UpdateValidations.class})
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the exercise type
     *
     * @return the exercise type
     */
    @NotNull(groups = {AllValidations.class, UpdateValidations.class})
    @Size(min = 1, max = 255, groups = {AllValidations.class, UpdateValidations.class})
    public String getType() {
        return type;
    }

    /**
     * Sets the exercise type
     *
     * @param type the new exercise type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the body part
     *
     * @return the body part
     */
    public String getBodyPart() {
        return bodyPart;
    }

    /**
     * Sets the body part
     *
     * @param bodyPart the new body part
     */
    public void setBodyPart(String bodyPart) {
        this.bodyPart = bodyPart;
    }

    /**
     * Gets the equipment
     *
     * @return the equipment
     */
    public String getEquipment() {
        return equipment;
    }

    /**
     * Sets the equipment
     *
     * @param equipment the new equipment
     */
    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    /**
     * Gets the link
     *
     * @return the link
     */
    @URL(groups = {AllValidations.class, UpdateValidations.class})
    public String getLink() {
        return link;
    }

    /**
     * Sets the link
     *
     * @param link the new link
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * Gets the trainerId.
     *
     * @return the trainerId
     */
    @NotNull(groups = {AllValidations.class, UpdateValidations.class})
    public Long getTrainerId() {
        return trainerId;
    }

    /**
     * Sets the trainerId.
     *
     * @param trainerId the new trainerId
     */
    public void setTrainerId(Long trainerId) {
        this.trainerId = trainerId;
    }

}
