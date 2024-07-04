package es.udc.fi.dc.tfg.rest.dtos;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Clase TemplateRowDto.
 */
public class TemplateRowDto {

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
     * Identificador de la plantilla de entrenamiento.
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
     * Identificador del ejercicio asociado a la fila de la plantilla.
     */
    private Long exerciseId;

    /**
     * Identificador de la plantilla donde se encuentra la fila.
     */
    private Long templateId;

    /**
     * Instantiates a new template row dto.
     */
    public TemplateRowDto() {
    }

    /**
     * Constructor de la clase TemplateRowDto.
     *
     * @param id El identificador de la fila de la plantilla.
     * @param series Las series a realizar del ejercicio.
     * @param repetitions Las repeticiones a realizar por serie.
     * @param weight El peso a levantar por repetición.
     * @param exerciseId El ID del ejercicio a realizar.
     * @param templateId El ID de la plantilla donde se encuentra la fila.
     */
    public TemplateRowDto(Long id, Integer series, Integer repetitions,
            BigDecimal weight, Long exerciseId, Long templateId) {

        this.id = id;
        this.series = series;
        this.repetitions = repetitions;
        this.weight = weight;
        this.exerciseId = exerciseId;
        this.templateId = templateId;

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
     * Gets the exerciseId.
     *
     * @return the exerciseId
     */
    @NotNull(groups = {AllValidations.class, UpdateValidations.class})
    public Long getExerciseId() {
        return exerciseId;
    }

    /**
     * Sets the exerciseId.
     *
     * @param exerciseId the new exerciseId
     */
    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }

    /**
     * Gets the templateId.
     *
     * @return the templateId
     */
    @NotNull(groups = {AllValidations.class, UpdateValidations.class})
    public Long getTemplateId() {
        return templateId;
    }

    /**
     * Sets the templateId.
     *
     * @param templateId the new templateId
     */
    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

}
