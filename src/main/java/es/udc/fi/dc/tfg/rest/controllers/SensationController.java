package es.udc.fi.dc.tfg.rest.controllers;

import static es.udc.fi.dc.tfg.rest.dtos.SensationConversor.toSensationDto;
import static es.udc.fi.dc.tfg.rest.dtos.SensationConversor.toSensationsDto;
import static es.udc.fi.dc.tfg.rest.dtos.SensationConversor.toSensations;

import es.udc.fi.dc.tfg.model.common.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.tfg.model.entities.Sensations;
import es.udc.fi.dc.tfg.model.entities.Templates;
import es.udc.fi.dc.tfg.model.entities.TrainingCycles;
import es.udc.fi.dc.tfg.model.entities.Users;
import es.udc.fi.dc.tfg.model.services.SensationService;
import es.udc.fi.dc.tfg.model.services.TemplateService;
import es.udc.fi.dc.tfg.model.services.TrainingCycleService;
import es.udc.fi.dc.tfg.model.services.UserService;
import es.udc.fi.dc.tfg.model.services.exceptions.InvalidRoleException;
import es.udc.fi.dc.tfg.model.services.exceptions.PermissionException;
import es.udc.fi.dc.tfg.rest.dtos.SensationDto;
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

/**
 * Clase SensationController.
 */
@RestController
@RequestMapping("/api/sensations")
public class SensationController {

    @Autowired
    private TemplateService templateService;

    @Autowired
    private TrainingCycleService cycleService;

    @Autowired
    private UserService userService;

    @Autowired
    private SensationService sensationService;

    // Método para validar un registro de sensaciones
    private void validateSensationUser(Long userId, Long clientId, TrainingCycles cycle)
            throws InstanceNotFoundException, PermissionException, InvalidRoleException {

        // Comprobamos si el usuario que realiza la petición es el cliente 
        // del ciclo de la plantilla donde se registran las sensaciones
        userService.validateUser(userId, clientId);

        // Comprobamos si el usuario que realiza la petición
        // es el cliente asignado a las sensaciones
        userService.validateUser(userId, cycle.getClient().getId());

        // Por la propiedad transitiva de la igualdad
        // omitimos comprobar que el cliente del ciclo
        // es el cliente asignado a las sensaciones
    }

    /**
     * Crea un nuevo registro de sensaciones.
     *
     * @param userId el ID del usuario que realiza la petición
     * @param sensationDto el DTO de las sensaciones
     * @return una ResponseEntity que contiene un SensationDto
     * @throws InstanceNotFoundException si no se encuentra un usuario con el ID
     * proporcionado
     * @throws PermissionException si el ID del usuario que realiza la petición
     * no coincide con el ID del entrenador que crea el ciclo
     * @throws InvalidRoleException si el usuario que se va validar no tiene rol
     */
    @PostMapping("/create")
    public ResponseEntity<SensationDto> sensationsRegister(@RequestAttribute Long userId,
            @Validated({SensationDto.AllValidations.class}) @RequestBody SensationDto sensationDto)
            throws InstanceNotFoundException, PermissionException, InvalidRoleException {

        Users client = userService.loginFromId(userId);
        Templates template = templateService.getTemplateInfo(sensationDto.getTemplateId());
        TrainingCycles cycle = cycleService.getCycleInfo(template.getCycle().getId());

        validateSensationUser(client.getId(), sensationDto.getClientId(), cycle);

        Sensations sensations = toSensations(sensationDto, template, client);

        sensationService.sensationsRegister(sensations);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(sensations.getId()).toUri();

        return ResponseEntity.created(location).body(toSensationDto(sensations));

    }

    /**
     * Devuelve la lista de registros de sensaciones de un cliente.
     *
     * @param userId el ID del usuario que realiza la petición
     * @param clientId el ID del ciclo
     * @return una lista de registros
     * @throws InstanceNotFoundException si no se encuentra un ciclo con el ID
     * proporcionado
     * @throws PermissionException si el ID del usuario que realiza la petición
     * no coincide con el ID del entrenador de los ciclos a obtener
     * @throws InvalidRoleException si el usuario que se va validar no tiene rol
     */
    @GetMapping("/fromClient/{clientId}")
    public List<SensationDto> getSensations(@RequestAttribute Long userId,
            @PathVariable("clientId") Long clientId) throws InstanceNotFoundException,
            PermissionException, InvalidRoleException {

        userService.validateUser(userId, clientId);

        List<Sensations> sensations = sensationService.getSensations(clientId);

        return toSensationsDto(sensations);

    }

    /**
     * Devuelve el registro de sensaciones a partir del ID de la plantilla.
     *
     * @param userId el ID del usuario que realiza la petición
     * @param templateId el ID de la plantilla asociada al registro de
     * sensaciones
     * @return una plantilla
     * @throws InstanceNotFoundException si no se encuentra una plantilla con el
     * ID proporcionado
     * @throws PermissionException si el ID del usuario que realiza la petición
     * no coincide con el ID del entrenador de los ciclos a obtener
     * @throws InvalidRoleException si el usuario que se va validar no tiene rol
     */
    @GetMapping("/{id}")
    public SensationDto getSensationInfo(@RequestAttribute Long userId,
            @PathVariable("id") Long templateId) throws InstanceNotFoundException,
            PermissionException, InvalidRoleException {

        Sensations sensations = sensationService.getSensationInfo(templateId);
        Templates template = templateService.getTemplateInfo(sensations.getTemplate().getId());
        TrainingCycles cycle = cycleService.getCycleInfo(template.getCycle().getId());

        validateSensationUser(userId, sensations.getClient().getId(), cycle);

        return toSensationDto(sensations);

    }

    /**
     * Edita un registro de sensaciones.
     *
     * @param userId el ID del usuario que realiza la petición
     * @param id el ID del registro que se va a editar
     * @param sensationDto el sensation dto
     * @return el sensation dto
     * @throws InstanceNotFoundException si no se encuentra un registro con el
     * ID proporcionado
     * @throws PermissionException si el ID del usuario que realiza la petición
     * no coincide con el ID del usuario al que se le va a actualizar el perfil
     * @throws InvalidRoleException si el usuario que se va validar no tiene rol
     */
    @PutMapping("/{id}/update")
    public SensationDto updateSensations(@RequestAttribute Long userId, @PathVariable("id") Long id,
            @Validated({SensationDto.UpdateValidations.class}) @RequestBody SensationDto sensationDto)
            throws InstanceNotFoundException, PermissionException, InvalidRoleException {

        Templates template = templateService.getTemplateInfo(sensationDto.getTemplateId());
        TrainingCycles cycle = cycleService.getCycleInfo(template.getCycle().getId());

        validateSensationUser(userId, sensationDto.getClientId(), cycle);

        return toSensationDto(sensationService.updateSensations(id, sensationDto.getFatigue(),
                sensationDto.getStiffness(), sensationDto.getMotivation(), sensationDto.getSleep()));

    }

}
