package es.udc.fi.dc.tfg.model.entities;

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
 * Clase Sensations que representa las sensaciones durante el entrenamiento de
 * un cliente.
 */
@Entity
@Table(name = "Sensations")
public class Sensations {

    /**
     * Identificador único autogenerado de la fila de una plantilla.
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
    private LocalDateTime sensationDate;

    /**
     * Plantilla asociada a las sensaciones.
     */
    private Templates template;

    /**
     * Cliente asociado a las sensaciones.
     */
    private Users client;

    /**
     * Constructor vacío de la clase Sensations.
     */
    public Sensations() {
    }

    /**
     * Constructor de la clase Sensations.
     *
     * @param fatigue La fatiga del cliente durante el entreno.
     * @param stiffness Las agujetas del cliente durante el entreno.
     * @param motivation La motivación del cliente durante el entreno.
     * @param sleep La calidad de sueño del cliente previa al entreno.
     * @param sensationDate La fecha en la que se registran las sensaciones.
     * @param template La plantilla asociada a las sensaciones.
     * @param client El cliente asociado a las sensaciones.
     */
    public Sensations(Integer fatigue, Integer stiffness,
            Integer motivation, Integer sleep, LocalDateTime sensationDate,
            Templates template, Users client) {

        this.fatigue = fatigue;
        this.stiffness = stiffness;
        this.motivation = motivation;
        this.sleep = sleep;
        this.sensationDate = sensationDate;
        this.template = template;
        this.client = client;

    }

    /**
     * Gets the sensation id.
     *
     * @return the sensation id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    /**
     * Sets the sensation id.
     *
     * @param id the new sensation id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the fatigue.
     *
     * @return the fatigue
     */
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
    public LocalDateTime getSensationDate() {
        return sensationDate;
    }

    /**
     * Sets the sensation date.
     *
     * @param sensationDate the new sensation date
     */
    public void setSensationDate(LocalDateTime sensationDate) {
        this.sensationDate = sensationDate;
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

    /**
     * Gets the client.
     *
     * @return the client
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
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
