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
import java.time.LocalDate;

/**
 * Clase TrainingCycles que representa los ciclos de entrenamiento.
 */
@Entity
@Table(name = "TrainingCycles")
public class TrainingCycles {

    /**
     * Identificador único autogenerado del ciclo de entrenamiento.
     */
    private Long id;

    /**
     * Nombre del ciclo de entrenamiento.
     */
    private String name;

    /**
     * Descripción del ciclo de entrenamiento, puede ser nulo.
     */
    private String description;

    /**
     * Fecha de inicio del ciclo de entrenamiento.
     */
    private LocalDate fromDate;

    /**
     * Fecha fin del ciclo de entrenamiento.
     */
    private LocalDate toDate;

    /**
     * Entrenador creador del ciclo de entrenamiento.
     */
    private Users trainer;

    /**
     * Cliente asignado al ciclo de entrenamiento.
     */
    private Users client;

    /**
     * Constructor vacío de la clase TrainingCycles.
     */
    public TrainingCycles() {
    }

    /**
     * Constructor de la clase TrainingCycles.
     *
     * @param name El nombre del ciclo.
     * @param description La descripción del ciclo.
     * @param fromDate La fecha de inicio del ciclo.
     * @param toDate La fecha fin del ciclo.
     * @param trainer El entrenador creador del ciclo.
     * @param client El cliente asignado al ciclo.
     */
    public TrainingCycles(String name, String description,
            LocalDate fromDate, LocalDate toDate, Users trainer, Users client) {
        this.name = name;
        this.description = description;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.trainer = trainer;
        this.client = client;
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
     * Gets the cycle name.
     *
     * @return the cycle name
     */
    @Column(name = "cycleName")
    public String getName() {
        return name;
    }

    /**
     * Sets the cycle name.
     *
     * @param name the new cycle name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the cycle description.
     *
     * @return the cycle description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the cycle description.
     *
     * @param description the new cycle description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the cycle initial date.
     *
     * @return the cycle initial date
     */
    public LocalDate getFromDate() {
        return fromDate;
    }

    /**
     * Sets the cycle initial date.
     *
     * @param fromDate the new cycle initial date
     */
    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * Gets the cycle end date.
     *
     * @return the cycle end date
     */
    public LocalDate getToDate() {
        return toDate;
    }

    /**
     * Sets the cycle end date.
     *
     * @param toDate the new cycle end date
     */
    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    /**
     * Gets the cycle trainer.
     *
     * @return the cycle trainer
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainerId")
    public Users getTrainer() {
        return trainer;
    }

    /**
     * Sets the cycle trainer.
     *
     * @param trainer the new cycle trainer
     */
    public void setTrainer(Users trainer) {
        this.trainer = trainer;
    }

    /**
     * Gets the cycle client.
     *
     * @return the cycle client
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clientId")
    public Users getClient() {
        return client;
    }

    /**
     * Sets the cycle client.
     *
     * @param client the new cycle client
     */
    public void setClient(Users client) {
        this.client = client;
    }

}
