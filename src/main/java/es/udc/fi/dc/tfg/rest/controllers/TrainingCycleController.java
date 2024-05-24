package es.udc.fi.dc.tfg.rest.controllers;

import static es.udc.fi.dc.tfg.rest.dtos.TrainingCycleConversor.toTrainingCycleDto;
import static es.udc.fi.dc.tfg.rest.dtos.TrainingCycleConversor.toTrainingCycles;
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
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
     * no coincide con el ID del usuario al que se le va a actualizar el perfil
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

}
