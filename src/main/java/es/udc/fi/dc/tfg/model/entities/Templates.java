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
import java.time.LocalDateTime;

/**
 * Clase Templates que representa las plantillas de entrenamiento.
 */
@Entity
@Table(name = "Templates")
public class Templates {

    /**
     * Identificador único autogenerado de la plantilla.
     */
    private Long id;

    /**
     * Nombre de la plantilla.
     */
    private String name;

    /**
     * Fecha de creación de la plantilla.
     */
    private LocalDateTime creationDate;

    /**
     * Ciclo de entrenamiento asociado a la plantilla.
     */
    private TrainingCycles cycle;

    /**
     * Constructor vacío de la clase Templates.
     */
    public Templates() {
    }

    /**
     * Constructor de la clase Templates.
     *
     * @param name El nombre de la plantilla.
     * @param creationDate La fecha de creación de la plantilla.
     * @param cycle El ciclo de entrenamiento asociado a la plantilla.
     */
    public Templates(String name, LocalDateTime creationDate, TrainingCycles cycle) {

        this.name = name;
        this.creationDate = creationDate;
        this.cycle = cycle;

    }

    /**
     * Gets the template id.
     *
     * @return the template id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    /**
     * Sets the template id.
     *
     * @param id the new template id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the template name.
     *
     * @return the template name
     */
    @Column(name = "templateName")
    public String getName() {
        return name;
    }

    /**
     * Sets the template name.
     *
     * @param name the new template name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the template creation date.
     *
     * @return the template creation date
     */
    @Column(name = "templateDate")
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Sets the template creation date.
     *
     * @param creationDate the new template creation date
     */
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Gets the training cycle.
     *
     * @return the training cycle
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cycleId")
    public TrainingCycles getCycle() {
        return cycle;
    }

    /**
     * Sets the training cycle.
     *
     * @param cycle the new training cycle
     */
    public void setCycle(TrainingCycles cycle) {
        this.cycle = cycle;
    }

}
