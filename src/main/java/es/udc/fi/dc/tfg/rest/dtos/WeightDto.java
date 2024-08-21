package es.udc.fi.dc.tfg.rest.dtos;

import java.math.BigDecimal;

/**
 * Clase WeightDto.
 */
public class WeightDto {

    /**
     * Identificador del registro.
     */
    private Long id;

    /**
     * Peso del cliente en kg, puede ser nulo.
     */
    private BigDecimal weight;

    /**
     * Fecha en la que se registró el peso.
     */
    private String weightDate;

    /**
     * Identificador del cliente asignado al registro.
     */
    private Long clientId;

    /**
     * Instantiates a new weight dto.
     */
    public WeightDto() {
    }

    /**
     * Constructor para usuarios de la clase UserDto.
     *
     * @param id El identificador del usuario.
     * @param weight El peso corporal del cliente.
     * @param weightDate La fecha de registro del peso.
     * @param clientId El ID del cliente asignado al registro.
     */
    public WeightDto(Long id, BigDecimal weight, String weightDate, Long clientId) {

        this.id = id;
        this.weight = weight;
        this.weightDate = weightDate;
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
     * Gets the weight date.
     *
     * @return the weight date
     */
    public String getWeightDate() {
        return weightDate;
    }

    /**
     * Sets the weight date.
     *
     * @param weightDate the new weight date
     */
    public void setWeightDate(String weightDate) {
        this.weightDate = weightDate;
    }

    /**
     * Gets the clientId.
     *
     * @return the clientId
     */
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
