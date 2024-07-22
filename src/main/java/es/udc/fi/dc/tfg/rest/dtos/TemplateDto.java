package es.udc.fi.dc.tfg.rest.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Clase TemplateDto.
 */
public class TemplateDto {

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
     * Nombre de la plantilla de entrenamiento.
     */
    private String name;

    /**
     * Fecha de creación de la plantilla de entrenamiento.
     */
    private String creationDate;

    /**
     * Identificador del ciclo donde se crea la plantilla de entrenamiento.
     */
    private Long cycleId;

    /**
     * Instantiates a new template dto.
     */
    public TemplateDto() {
    }

    /**
     * Constructor de la clase TemplateDto.
     *
     * @param id El identificador del ciclo.
     * @param name El nombre del ciclo.
     * @param creationDate La fecha de creación de la plantilla.
     * @param cycleId El ID del ciclo donde se crea la plantilla.
     */
    public TemplateDto(Long id, String name, String creationDate, Long cycleId) {

        this.id = id;
        this.name = name;
        this.creationDate = creationDate;
        this.cycleId = cycleId;

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
     * Gets the template name.
     *
     * @return the template name
     */
    @NotNull(groups = {AllValidations.class, UpdateValidations.class})
    @Size(min = 1, max = 255, groups = {AllValidations.class, UpdateValidations.class})
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
     * Gets the creation date.
     *
     * @return the creation date
     */
    @NotNull(groups = {AllValidations.class, UpdateValidations.class})
    public String getCreationDate() {
        return creationDate;
    }

    /**
     * Sets the creation date.
     *
     * @param creationDate the new creation date
     */
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Gets the cycleId.
     *
     * @return the cycleId
     */
    @NotNull(groups = {AllValidations.class, UpdateValidations.class})
    public Long getCycleId() {
        return cycleId;
    }

    /**
     * Sets the cycleId.
     *
     * @param cycleId the new cycleId
     */
    public void setCycleId(Long cycleId) {
        this.cycleId = cycleId;
    }

}
