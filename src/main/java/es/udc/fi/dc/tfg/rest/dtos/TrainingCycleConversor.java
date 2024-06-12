package es.udc.fi.dc.tfg.rest.dtos;

import es.udc.fi.dc.tfg.model.entities.TrainingCycles;
import es.udc.fi.dc.tfg.model.entities.Users;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase TrainingCycleConversor.
 */
public class TrainingCycleConversor {

    /**
     * Instancia un nuevo training cycle conversor
     */
    private TrainingCycleConversor() {
    }

    /**
     * Convierte un objeto TrainingCycles a un objeto TrainingCycleDto
     *
     * @param cycle el objeto TrainingCycles a convertir
     * @return un nuevo objeto TrainingCycleDto que contiene los mismos datos
     * que el objeto TrainingCycles proporcionado
     */
    public static final TrainingCycleDto toTrainingCycleDto(TrainingCycles cycle) {
        return new TrainingCycleDto(cycle.getId(), cycle.getName(),
                cycle.getDescription(), cycle.getFromDate().toString(),
                cycle.getToDate().toString(), cycle.getTrainer().getId(),
                cycle.getClient().getId());
    }

    /**
     * Convierte una lista de objetos TrainingCycles a objetos TrainingCycleDto.
     *
     * @param cycles la lista de objetos TrainingCycles a convertir
     * @return una nueva lista de objetos TrainingCycleDto que contiene los
     * mismos datos que la lista de objetos TrainingCycles proporcionada
     */
    public static final List<TrainingCycleDto> toTrainingCyclesDto(List<TrainingCycles> cycles) {
        return cycles.stream().map(cycle -> {
            return toTrainingCycleDto(cycle);
        }).collect(Collectors.toList());
    }

    /**
     * Convierte un objeto TrainingCycleDto a un objeto TrainingCycles
     *
     * @param cycleDto el objeto TrainingCycleDto a convertir
     * @param trainer el entrenador del TrainingCycleDto
     * @param client el cliente del TrainingCycleDto
     * @return un nuevo objeto TrainingCycles que contiene los mismos datos que
     * el objeto TrainingCycleDto proporcionado
     */
    public static final TrainingCycles toTrainingCycles(
            TrainingCycleDto cycleDto, Users trainer, Users client) {
        return new TrainingCycles(cycleDto.getName(), cycleDto.getDescription(),
                LocalDate.parse(cycleDto.getFromDate()), LocalDate.parse(cycleDto.getToDate()),
                trainer, client);
    }

}
