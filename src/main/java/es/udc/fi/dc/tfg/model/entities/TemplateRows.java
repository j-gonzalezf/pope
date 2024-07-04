package es.udc.fi.dc.tfg.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;

/**
 * Clase TemplateRows que representa las filas de una plantilla de
 * entrenamiento.
 */
@Entity
@Table(name = "TemplateRows")
public class TemplateRows {

    /**
     * Identificador único autogenerado de la fila de una plantilla.
     */
    private Long id;

    /**
     * Series del ejercicio, puede ser nulo.
     */
    private Integer series;

    /**
     * Repeticiones del ejercicio, puede ser nulo.
     */
    private Integer repetitions;

    /**
     * Peso a levantar en el ejercicio, puede ser nulo.
     */
    private BigDecimal weight;

    /**
     * Ejercicio asociado a la fila de la plantilla.
     */
    private Exercises exercise;

    /**
     * Plantilla asociada a la fila de la plantilla.
     */
    private Templates template;

    /**
     * Constructor vacío de la clase TemplateRows.
     */
    public TemplateRows() {
    }

    /**
     * Constructor de la clase TemplateRows.
     *
     * @param series Las series a hacer del ejercicio.
     * @param repetitions Las repeticiones a hacer del ejercicio.
     * @param weight El peso a levantar en el ejercicio.
     * @param exercise El ejercicio asociado a la fila de la plantilla.
     * @param template La plantilla asociada a la fila de la plantilla.
     */
    public TemplateRows(Integer series, Integer repetitions, BigDecimal weight,
            Exercises exercise, Templates template) {

        this.series = series;
        this.repetitions = repetitions;
        this.weight = weight;
        this.exercise = exercise;
        this.template = template;

    }

    /**
     * Gets the template row id.
     *
     * @return the template row id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    /**
     * Sets the template row id.
     *
     * @param id the new template row id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the series.
     *
     * @return the series
     */
    public Integer getSeries() {
        return series;
    }

    /**
     * Sets the series.
     *
     * @param series the new series
     */
    public void setSeries(Integer series) {
        this.series = series;
    }

    /**
     * Gets the repetitions.
     *
     * @return the repetitions
     */
    public Integer getRepetitions() {
        return repetitions;
    }

    /**
     * Sets the repetitions.
     *
     * @param repetitions the new repetitions
     */
    public void setRepetitions(Integer repetitions) {
        this.repetitions = repetitions;
    }

    /**
     * Gets the weight.
     *
     * @return the weight
     */
    public BigDecimal getWeight() {
        return weight;
    }

    /**
     * Sets the weight.
     *
     * @param weight the new weight
     */
    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    /**
     * Gets the exercise.
     *
     * @return the exercise
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exerciseId")
    public Exercises getExercise() {
        return exercise;
    }

    /**
     * Sets the exercise.
     *
     * @param exercise the new exercise
     */
    public void setExercise(Exercises exercise) {
        this.exercise = exercise;
    }

    /**
     * Gets the template.
     *
     * @return the template
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "templateId")
    public Templates getTemplate() {
        return template;
    }

    /**
     * Sets the template.
     *
     * @param template the new template
     */
    public void setTemplate(Templates template) {
        this.template = template;
    }

}
