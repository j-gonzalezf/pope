package es.udc.fi.dc.tfg.rest.controllers;

import static es.udc.fi.dc.tfg.rest.dtos.TrainingCycleConversor.toTrainingCycleDto;
import static es.udc.fi.dc.tfg.rest.dtos.TrainingCycleConversor.toTrainingCyclesDto;
import static es.udc.fi.dc.tfg.rest.dtos.TrainingCycleConversor.toTrainingCycles;
import java.net.URI;
import java.util.List;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import es.udc.fi.dc.tfg.model.services.exceptions.InvalidRoleException;
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

    // Método para validar un ciclo
    private void validateCycleUser(Long userId, Users trainer, Users client)
            throws InstanceNotFoundException, PermissionException, InvalidRoleException {

        // Comprobamos si el usuario que realiza la petición es el entrenador 
        // del ciclo en el que se va a realizar la operación CRUD
        userService.validateUser(userId, trainer.getId());

        // Comprobamos si el usuario que realiza la petición
        // es el entrenador del cliente asignado al ciclo
        userService.validateUser(userId, client.getId());

        // Por la propiedad transitiva de la igualdad
        // omitimos comprobar que el entrenador del ciclo
        // es el entrenador del cliente asignado al ciclo
    }

    /**
     * Crea un nuevo ciclo de entrenamiento.
     *
     * @param userId el ID del usuario que realiza la petición
     * @param cycleDto el DTO del ciclo
     * @return una ResponseEntity que contiene un TrainingCycleDto
     * @throws InstanceNotFoundException si no se encuentra un usuario con el ID
     * proporcionado
     * @throws PermissionException si el ID del usuario que realiza la petición
     * no coincide con el ID del entrenador que crea el ciclo
     * @throws InvalidRoleException si el usuario que se va validar no tiene rol
     */
    @PostMapping("/cycle/create")
    public ResponseEntity<TrainingCycleDto> createTrainingCycle(@RequestAttribute Long userId,
            @Validated({TrainingCycleDto.AllValidations.class}) @RequestBody TrainingCycleDto cycleDto)
            throws InstanceNotFoundException, PermissionException, InvalidRoleException {

        Users trainer = userService.loginFromId(cycleDto.getTrainerId());
        Users client = userService.loginFromId(cycleDto.getClientId());

        validateCycleUser(userId, trainer, client);

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
     * @throws InstanceNotFoundException si no se encuentra un usuario con el ID
     * proporcionado
     * @throws PermissionException si el ID del usuario que realiza la petición
     * no coincide con el ID del entrenador de los ciclos a obtener
     * @throws InvalidRoleException si el usuario que se va validar no tiene rol
     */
    @GetMapping("/{trainerId}/clients/{clientId}/cycles")
    public List<TrainingCycleDto> getTrainingCycles(@RequestAttribute Long userId,
            @PathVariable("trainerId") Long trainerId, @PathVariable("clientId") Long clientId)
            throws InstanceNotFoundException, PermissionException, InvalidRoleException {

        Users user = userService.loginFromId(userId);
        Users trainer = userService.loginFromId(trainerId);
        Users client = userService.loginFromId(clientId);

        if (user.getUserRole().toString().equals("CLIENT")) {
            validateCycleUser(user.getTrainer().getId(), trainer, client);
        } else {
            validateCycleUser(userId, trainer, client);
        }

        List<TrainingCycles> cycles = cycleService.getCycles(trainerId, clientId);

        return toTrainingCyclesDto(cycles);

    }

    /**
     * Devuelve el ciclo de un entrenador a partir de su ID.
     *
     * @param userId el ID del usuario que realiza la petición
     * @param cycleId el ID del ciclo
     * @return un ciclo
     * @throws InstanceNotFoundException si no se encuentra un usuario con el ID
     * proporcionado
     * @throws PermissionException si el ID del usuario que realiza la petición
     * no coincide con el ID del entrenador de los ciclos a obtener
     * @throws InvalidRoleException si el usuario que se va validar no tiene rol
     */
    @GetMapping("/cycle/{id}")
    public TrainingCycleDto getCycleInfo(@RequestAttribute Long userId,
            @PathVariable("id") Long cycleId) throws InstanceNotFoundException,
            PermissionException, InvalidRoleException {

        Users user = userService.loginFromId(userId);
        TrainingCycles cycle = cycleService.getCycleInfo(cycleId);
        Users trainer = userService.loginFromId(cycle.getTrainer().getId());
        Users client = userService.loginFromId(cycle.getClient().getId());

        if (user.getUserRole().toString().equals("CLIENT")) {
            validateCycleUser(user.getTrainer().getId(), trainer, client);
        } else {
            validateCycleUser(userId, trainer, client);
        }

        return toTrainingCycleDto(cycle);

    }

    /**
     * Edita un ciclo.
     *
     * @param userId el ID del usuario que realiza la petición
     * @param id el ID del ciclo que se va a editar
     * @param cycleDto el training cycle dto
     * @return el training cycle dto
     * @throws InstanceNotFoundException si no se encuentra un ciclo con el ID
     * proporcionado
     * @throws PermissionException si el ID del usuario que realiza la petición
     * no coincide con el ID del usuario al que se le va a actualizar el perfil
     * @throws InvalidRoleException si el usuario que se va validar no tiene rol
     */
    @PutMapping("/{id}")
    public TrainingCycleDto updateTrainingCycle(@RequestAttribute Long userId, @PathVariable("id") Long id,
            @Validated({TrainingCycleDto.UpdateValidations.class}) @RequestBody TrainingCycleDto cycleDto)
            throws InstanceNotFoundException, PermissionException, InvalidRoleException {

        Users trainer = userService.loginFromId(cycleDto.getTrainerId());
        Users client = userService.loginFromId(cycleDto.getClientId());

        validateCycleUser(userId, trainer, client);

        return toTrainingCycleDto(cycleService.updateCycle(id, cycleDto.getName(),
                cycleDto.getDescription(), LocalDate.parse(cycleDto.getFromDate()),
                LocalDate.parse(cycleDto.getToDate())));

    }

    /**
     * Elimina un ciclo.
     *
     * @param userId el ID del usuario que realiza la petición
     * @param id el ID del ciclo que se va a eliminar
     * @return el ID del ciclo que ha sido eliminado
     * @throws InstanceNotFoundException si no se encuentra un ciclo con el ID
     * proporcionado
     * @throws PermissionException si el ID del usuario que realiza la petición
     * no coincide con el ID del ciclo que se va a eliminar
     * @throws InvalidRoleException si el usuario no tiene rol
     */
    @DeleteMapping("/cycle/{id}/delete")
    public Long deleteCycle(@RequestAttribute Long userId, @PathVariable("id") Long id)
            throws InstanceNotFoundException, PermissionException, InvalidRoleException {

        TrainingCycles cycle = cycleService.getCycleInfo(id);

        validateCycleUser(userId, cycle.getTrainer(), cycle.getClient());

        return cycleService.deleteCycle(id);

    }

}
