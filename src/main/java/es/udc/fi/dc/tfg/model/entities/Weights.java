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
import java.time.LocalDateTime;

/**
 * Clase Weights que representa el historial de pesos de un cliente.
 */
@Entity
@Table(name = "Weights")
public class Weights {

    /**
     * Identificador único autogenerado del peso de un cliente.
     */
    private Long id;

    /**
     * Masa corporal del cliente.
     */
    private BigDecimal weight;

    /**
     * Fecha en la que se registró el peso.
     */
    private LocalDateTime weightDate;

    /**
     * Cliente asociado al peso registrado.
     */
    private Users client;

    /**
     * Constructor vacío de la clase Weights.
     */
    public Weights() {
    }

    /**
     * Constructor de la clase Weights.
     *
     * @param weight La masa corporal del cliente.
     * @param weightDate La fecha de registro del peso.
     * @param client El cliente asociada al peso registrado.
     */
    public Weights(BigDecimal weight, LocalDateTime weightDate, Users client) {

        this.weight = weight;
        this.weightDate = weightDate;
        this.client = client;

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
    public LocalDateTime getWeightDate() {
        return weightDate;
    }

    /**
     * Sets the weight date.
     *
     * @param weightDate the new weight date
     */
    public void setWeightDate(LocalDateTime weightDate) {
        this.weightDate = weightDate;
    }

    /**
     * Gets the client.
     *
     * @return the client
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clientId")
    public Users getClient() {
        return client;
    }

    /**
     * Sets the client.
     *
     * @param client the new client
     */
    public void setClient(Users client) {
        this.client = client;
    }

}
