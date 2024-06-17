package es.udc.fi.dc.tfg.rest.controllers;

import static es.udc.fi.dc.tfg.rest.dtos.TrainingCycleConversor.toTrainingCycleDto;
import static es.udc.fi.dc.tfg.rest.dtos.TrainingCycleConversor.toTrainingCyclesDto;
import static es.udc.fi.dc.tfg.rest.dtos.TrainingCycleConversor.toTrainingCycles;
import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.udc.fi.dc.tfg.model.common.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.tfg.model.entities.TrainingCycles;
import es.udc.fi.dc.tfg.model.entities.Users;
import es.udc.fi.dc.tfg.model.services.TrainingCycleService;
import es.udc.fi.dc.tfg.model.services.UserService;
import es.udc.fi.dc.tfg.model.services.exceptions.PermissionException;
import es.udc.fi.dc.tfg.rest.dtos.TrainingCycleDto;
import java.time.LocalDate;

/**
 * Clase TrainingCycleController.
 */
@RestController
@RequestMapping("/api/templates")
public class TrainingCycleController {

    @Autowired
    private TrainingCycleService cycleService;

    @Autowired
    private UserService userService;

    // Método para validar un usuario, userId siempre es un TRAINER
    private void validateUser(Long userId, Users trainer, Users client)
            throws PermissionException {

        try {

            // Comprobamos si el usuario que realiza la petición es el entrenador
            // del ciclo que se va a crear
            if (!trainer.getId().equals(userId)) {
                throw new PermissionException();
            }

            // Comprobamos si el usuario que realiza la petición es el entrenador
            // del cliente asignado al ciclo que se va a crear
            if (!client.getTrainer().getId().equals(userId)) {
                throw new PermissionException();
            }

            // Por la propiedad transitiva de la igualdad omitimos comprobar
            // que el entrenador del ciclo que se va a crear es el entrenador del 
            // cliente asignado al ciclo que se va a crear
        } catch (NullPointerException e) {
            throw new PermissionException();
        }

    }

    /**
     * Crear un nuevo ciclo de entrenamiento.
     *
     * @param userId el ID del usuario que realiza la petición
     * @param cycleDto el DTO del ciclo
     * @return una ResponseEntity que contiene un TrainingCycleDto
     * @throws InstanceNotFoundException si no se encuentra un usuario con el ID
     * proporcionado
     * @throws PermissionException si el ID del usuario que realiza la petición
     * no coincide con el ID del entrenador que crea el ciclo
     */
    @PostMapping("/cycle/create")
    public ResponseEntity<TrainingCycleDto> createTrainingCycle(@RequestAttribute Long userId,
            @Validated({TrainingCycleDto.AllValidations.class}) @RequestBody TrainingCycleDto cycleDto)
            throws InstanceNotFoundException, PermissionException {

        Users trainer = userService.loginFromId(cycleDto.getTrainerId());
        Users client = userService.loginFromId(cycleDto.getClientId());

        validateUser(userId, trainer, client);

        TrainingCycles cycle = toTrainingCycles(cycleDto, trainer, client);

        cycleService.createCycle(cycle);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(cycle.getId()).toUri();

        return ResponseEntity.created(location).body(toTrainingCycleDto(cycle));

    }

    /**
     * Devuelve la lista de ciclos de entrenamiento del cliente de un
     * entrenador.
     *
     * @param userId el ID del usuario que realiza la petición
     * @param trainerId el ID del entrenador
     * @param clientId el ID del cliente
     * @return una lista de ciclos
     * @throws PermissionException si el ID del usuario que realiza la petición
     * no coincide con el ID del entrenador
     * @throws InstanceNotFoundException si no se encuentra ningún cliente
     */
    @GetMapping("/{trainerId}/clients/{clientId}/cycles")
    public List<TrainingCycleDto> getTrainingCycles(@RequestAttribute Long userId,
            @PathVariable("trainerId") Long trainerId, @PathVariable("clientId") Long clientId)
            throws PermissionException, InstanceNotFoundException {

        Users trainer = userService.loginFromId(trainerId);
        Users client = userService.loginFromId(clientId);

        validateUser(userId, trainer, client);

        List<TrainingCycles> cycles = cycleService.getCycles(trainerId, clientId);

        return toTrainingCyclesDto(cycles);

    }

    /**
     * Devuelve el ciclo de un entrenador a partir de su ID.
     *
     * @param userId el ID del usuario que realiza la petición
     * @param cycleId el ID del ciclo
     * @return un ciclo
     * @throws PermissionException si el ID del usuario que realiza la petición
     * no coincide con el trainer ID del ciclo que se solicita
     * @throws InstanceNotFoundException si no se encuentra ningún cliente.
     */
    @GetMapping("/cycle/{id}")
    public TrainingCycleDto getClientInfo(@RequestAttribute Long userId,
            @PathVariable("id") Long cycleId) throws PermissionException,
            InstanceNotFoundException {

        Users trainer = userService.loginFromId(userId);
        TrainingCycles cycle = cycleService.getCycleInfo(cycleId);

        if (!trainer.getId().equals(cycle.getTrainer().getId())) {
            throw new PermissionException();
        }

        return toTrainingCycleDto(cycle);

    }

    /**
     * Editar ciclo.
     *
     * @param userId el ID del usuario que realiza la petición
     * @param id el ID del ciclo que se va a editar
     * @param cycleDto el training cycle dto
     * @return el user dto
     * @throws InstanceNotFoundException si no se encuentra un usuario con el ID
     * proporcionado
     * @throws PermissionException si el ID del usuario que realiza la petición
     * no coincide con el ID del usuario al que se le va a actualizar el perfil
     */
    @PutMapping("/{id}")
    public TrainingCycleDto updateTrainingCycle(@RequestAttribute Long userId, @PathVariable("id") Long id,
            @Validated({TrainingCycleDto.UpdateValidations.class}) @RequestBody TrainingCycleDto cycleDto)
            throws InstanceNotFoundException, PermissionException {

        Users trainer = userService.loginFromId(cycleDto.getTrainerId());
        Users client = userService.loginFromId(cycleDto.getClientId());

        validateUser(userId, trainer, client);

        return toTrainingCycleDto(cycleService.updateCycle(id, cycleDto.getName(),
                cycleDto.getDescription(), LocalDate.parse(cycleDto.getFromDate()),
                LocalDate.parse(cycleDto.getToDate())));

    }

}
