package es.udc.fi.dc.tfg.rest.dtos;

import es.udc.fi.dc.tfg.model.entities.Sensations;
import es.udc.fi.dc.tfg.model.entities.Templates;
import es.udc.fi.dc.tfg.model.entities.Users;
import java.time.LocalDateTime;

/**
 * Clase SensationConversor.
 */
public class SensationConversor {

    /**
     * Instancia un nuevo sensation conversor
     */
    private SensationConversor() {
    }

    /**
     * Convierte un objeto Sensations a un objeto SensationDto
     *
     * @param sensation el objeto Sensations a convertir
     * @return un nuevo objeto SensationDto que contiene los mismos datos que el
     * objeto Sensations proporcionado
     */
    public static final SensationDto toSensationDto(Sensations sensation) {
        return new SensationDto(sensation.getId(), sensation.getFatigue(),
                sensation.getStiffness(), sensation.getMotivation(),
                sensation.getSleep(), sensation.getSensationDate().toString(),
                sensation.getTemplate().getId(), sensation.getClient().getId());
    }

    /**
     * Convierte un objeto SensationDto a un objeto Sensations
     *
     * @param sensationDto el objeto SensationDto a convertir
     * @param template la plantilla SensationDto
     * @param client el cliente del SensationDto
     * @return un nuevo objeto Sensations que contiene los mismos datos que el
     * objeto SensationDto proporcionado
     */
    public static final Sensations toSensations(
            SensationDto sensationDto, Templates template, Users client) {
        return new Sensations(sensationDto.getFatigue(), sensationDto.getStiffness(),
                sensationDto.getMotivation(), sensationDto.getSleep(),
                LocalDateTime.parse(sensationDto.getSensationDate()),
                template, client);
    }

}
