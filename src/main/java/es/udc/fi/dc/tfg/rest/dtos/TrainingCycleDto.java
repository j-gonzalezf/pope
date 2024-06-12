package es.udc.fi.dc.tfg.rest.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Clase TrainingCycleDto.
 */
public class TrainingCycleDto {

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
     * Identificador del ciclo de entrenamiento.
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
    private String fromDate;

    /**
     * Fecha fin del ciclo de entrenamiento.
     */
    private String toDate;

    /**
     * Identificador del entrenador creador del ciclo de entrenamiento.
     */
    private Long trainerId;

    /**
     * Identificador del cliente asignado al ciclo de entrenamiento.
     */
    private Long clientId;

    /**
     * Instantiates a new cycle dto.
     */
    public TrainingCycleDto() {
    }

    /**
     * Constructor de la clase TrainingCycleDto.
     *
     * @param id El identificador del ciclo.
     * @param name El nombre del ciclo.
     * @param description La descripción del ciclo.
     * @param fromDate La fecha inicial del ciclo.
     * @param toDate La fecha fin del ciclo.
     * @param trainerId El ID del entrenador creador del ciclo.
     * @param clientId El ID del cliente asignado al ciclo.
     */
    public TrainingCycleDto(Long id, String name, String description,
            String fromDate, String toDate, Long trainerId, Long clientId) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.trainerId = trainerId;
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
     * Gets the cycle name.
     *
     * @return the cycle name
     */
    @NotNull(groups = {AllValidations.class, UpdateValidations.class})
    @Size(min = 1, max = 255, groups = {AllValidations.class, UpdateValidations.class})
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
     * Gets the description.
     *
     * @return the description
     */
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
     * Gets the initial date.
     *
     * @return the initial date
     */
    @NotNull(groups = {AllValidations.class, UpdateValidations.class})
    public String getFromDate() {
        return fromDate;
    }

    /**
     * Sets the initial date.
     *
     * @param fromDate the new initial date
     */
    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * Gets the end date.
     *
     * @return the end date
     */
    @NotNull(groups = {AllValidations.class, UpdateValidations.class})
    public String getToDate() {
        return toDate;
    }

    /**
     * Sets the end date.
     *
     * @param toDate the new end date
     */
    public void setToDate(String toDate) {
        this.toDate = toDate;
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

    /**
     * Gets the clientId.
     *
     * @return the clientId
     */
    @NotNull(groups = {AllValidations.class, UpdateValidations.class})
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
