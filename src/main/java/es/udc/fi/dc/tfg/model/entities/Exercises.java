package es.udc.fi.dc.tfg.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Clase Exercises que representa los ejercicios.
 */
@Entity
@Table(name = "Exercises")
public class Exercises {

    /**
     * Identificador único autogenerado del ejercicio.
     */
    private Long id;

    /**
     * Nombre del ejercicio.
     */
    private String name;

    /**
     * Descripción del ejercicio
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
     * URL a material multimedia que ilustra la correcta realización del
     * ejercicio, puede ser nulo.
     */
    private String link;

    /**
     * Entrenador creador del ejercio.
     */
    private Users trainer;

    /**
     * Constructor vacío de la clase Exercises
     */
    public Exercises() {
    }

    /**
     * Constructor de la clase Exercises
     *
     * @param name El nombre del ejercicio.
     * @param description La descripción del ejercicio.
     * @param type La categoría del ejercicio.
     * @param bodyPart La parte del cuerpo en la que se enfoca el ejercicio.
     * @param equipment El equipamiento necesario para realizar el ejercicio.
     * @param link La URL de referencia para la correcta realización del
     * ejercicio.
     * @param trainer El entrenador creador del ejercicio.
     */
    public Exercises(String name, String description, String type,
            String bodyPart, String equipment, String link, Users trainer) {

        this.name = name;
        this.description = description;
        this.type = type;
        this.bodyPart = bodyPart;
        this.equipment = equipment;
        this.link = link;
        this.trainer = trainer;

    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Column(name = "exerciseName")
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
     * Gets the exercise description.
     *
     * @return the exercise description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the exercise description.
     *
     * @param description the new exercise description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the exercise type
     *
     * @return the exercise type
     */
    @Column(name = "exerciseType")
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
     * Gets the exercise link
     *
     * @return the exercise link
     */
    public String getLink() {
        return link;
    }

    /**
     * Sets the exercise link
     *
     * @param link the new exercise link
     */
    @Column(name = "exerciseLink")
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * Gets the exercise trainer
     *
     * @return the exercise trainer
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainerId")
    public Users getTrainer() {
        return trainer;
    }

    /**
     * Sets the exercise trainer
     *
     * @param trainer the new exercise trainer
     */
    public void setTrainer(Users trainer) {
        this.trainer = trainer;
    }

}
