package es.udc.fi.dc.tfg.rest.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Clase SensationDto.
 */
public class SensationDto {

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
     * Fatiga durante el entreno, puede ser nulo.
     */
    private Integer fatigue;

    /**
     * Agujetas durante el entreno, puede ser nulo.
     */
    private Integer stiffness;

    /**
     * Motivación durante el entreno, puede ser nulo.
     */
    private Integer motivation;

    /**
     * Calidad de sueño, puede ser nulo.
     */
    private Integer sleep;

    /**
     * Fecha en la que se registraron las sensaciones.
     */
    private String sensationDate;

    /**
     * Identificador de la plantilla asociada a las sensaciones.
     */
    private Long templateId;

    /**
     * Identificador del cliente asignado a las sensaciones.
     */
    private Long clientId;

    /**
     * Instantiates a new senasation dto.
     */
    public SensationDto() {
    }

    /**
     * Constructor de la clase SensationDto.
     *
     * @param id El identificador de las sensaciones.
     * @param fatigue La fatiga durante el entreno.
     * @param stiffness Las agujetas durante el entreno.
     * @param motivation La motivación durante el entreno.
     * @param sleep La calidad de sueño del cliente.
     * @param sensationDate La fecha de registro de las sensaciones.
     * @param templateId El ID de la plantilla asociada a las sensaciones.
     * @param clientId El ID del cliente asignado a las sensaciones.
     */
    public SensationDto(Long id, Integer fatigue, Integer stiffness, Integer motivation,
            Integer sleep, String sensationDate, Long templateId, Long clientId) {

        this.id = id;
        this.fatigue = fatigue;
        this.stiffness = stiffness;
        this.motivation = motivation;
        this.sleep = sleep;
        this.sensationDate = sensationDate;
        this.templateId = templateId;
        this.clientId = clientId;

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
     * Gets the fatigue.
     *
     * @return the fatigue
     */
    @Max(value = 5, groups = {AllValidations.class, UpdateValidations.class})
    @Min(value = 0, groups = {AllValidations.class, UpdateValidations.class})
    public Integer getFatigue() {
        return fatigue;
    }

    /**
     * Sets the fatigue.
     *
     * @param fatigue the new fatigue
     */
    public void setFatigue(Integer fatigue) {
        this.fatigue = fatigue;
    }

    /**
     * Gets the stiffness.
     *
     * @return the stiffness
     */
    @Max(value = 5, groups = {AllValidations.class, UpdateValidations.class})
    @Min(value = 0, groups = {AllValidations.class, UpdateValidations.class})
    public Integer getStiffness() {
        return stiffness;
    }

    /**
     * Sets the stiffness.
     *
     * @param stiffness the new stiffness
     */
    public void setStiffness(Integer stiffness) {
        this.stiffness = stiffness;
    }

    /**
     * Gets the motivation.
     *
     * @return the motivation
     */
    @Max(value = 5, groups = {AllValidations.class, UpdateValidations.class})
    @Min(value = 0, groups = {AllValidations.class, UpdateValidations.class})
    public Integer getMotivation() {
        return motivation;
    }

    /**
     * Sets the motivation.
     *
     * @param motivation the new motivation
     */
    public void setMotivation(Integer motivation) {
        this.motivation = motivation;
    }

    /**
     * Gets the sleep.
     *
     * @return the sleep
     */
    @Max(value = 5, groups = {AllValidations.class, UpdateValidations.class})
    @Min(value = 0, groups = {AllValidations.class, UpdateValidations.class})
    public Integer getSleep() {
        return sleep;
    }

    /**
     * Sets the sleep.
     *
     * @param sleep the new sleep
     */
    public void setSleep(Integer sleep) {
        this.sleep = sleep;
    }

    /**
     * Gets the sensation date.
     *
     * @return the sensation date
     */
    @NotNull(groups = {SensationDto.AllValidations.class})
    public String getSensationDate() {
        return sensationDate;
    }

    /**
     * Sets the sensation date.
     *
     * @param sensationDate the new sensation date
     */
    public void setSensationDate(String sensationDate) {
        this.sensationDate = sensationDate;
    }

    /**
     * Gets the templateId.
     *
     * @return the templateId
     */
    @NotNull(groups = {SensationDto.AllValidations.class, SensationDto.UpdateValidations.class})
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

    /**
     * Gets the clientId.
     *
     * @return the clientId
     */
    @NotNull(groups = {SensationDto.AllValidations.class, SensationDto.UpdateValidations.class})
    public Long getClientId() {
        return clientId;
    }

    /**
     * Sets the clientId.
     *
     * @param clientId the new clientId
     */
    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

}
