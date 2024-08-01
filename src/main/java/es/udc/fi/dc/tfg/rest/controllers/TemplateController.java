package es.udc.fi.dc.tfg.rest.controllers;

import static es.udc.fi.dc.tfg.rest.dtos.TemplateConversor.toTemplateDto;
import static es.udc.fi.dc.tfg.rest.dtos.TemplateConversor.toTemplatesDto;
import static es.udc.fi.dc.tfg.rest.dtos.TemplateConversor.toTemplates;
import static es.udc.fi.dc.tfg.rest.dtos.TemplateConversor.toTemplateRowDto;
import static es.udc.fi.dc.tfg.rest.dtos.TemplateConversor.toTemplateRowsDto;
import static es.udc.fi.dc.tfg.rest.dtos.TemplateConversor.toTemplateRows;
import java.net.URI;
import java.util.List;

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
import es.udc.fi.dc.tfg.model.entities.Exercises;
import es.udc.fi.dc.tfg.model.entities.TemplateRows;
import es.udc.fi.dc.tfg.model.entities.Templates;
import es.udc.fi.dc.tfg.model.entities.TrainingCycles;
import es.udc.fi.dc.tfg.model.entities.Users;
import es.udc.fi.dc.tfg.model.services.ExerciseService;
import es.udc.fi.dc.tfg.model.services.TemplateService;
import es.udc.fi.dc.tfg.model.services.TrainingCycleService;
import es.udc.fi.dc.tfg.model.services.UserService;
import es.udc.fi.dc.tfg.model.services.exceptions.InvalidRoleException;
import es.udc.fi.dc.tfg.model.services.exceptions.PermissionException;
import es.udc.fi.dc.tfg.rest.dtos.TemplateDto;
import es.udc.fi.dc.tfg.rest.dtos.TemplateRowDto;

/**
 * Clase TemplateController.
 */
@RestController
@RequestMapping("/api/templates")
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    @Autowired
    private TrainingCycleService cycleService;

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private UserService userService;

    // Método para validar una plantilla
    private void validateTemplateUser(Long userId, TrainingCycles cycle)
            throws InstanceNotFoundException, PermissionException, InvalidRoleException {

        // Comprobamos si el usuario que realiza la petición es el entrenador 
        // del ciclo donde se crea la plantilla
        userService.validateUser(userId, cycle.getTrainer().getId());

        // Comprobamos si el usuario que realiza la petición
        // es el entrenador del cliente asignado al ciclo
        userService.validateUser(userId, cycle.getClient().getId());

        // Por la propiedad transitiva de la igualdad
        // omitimos comprobar que el entrenador del ciclo
        // es el entrenador del cliente asignado al ciclo
    }

    /**
     * Crea una nueva plantilla de entrenamiento.
     *
     * @param userId el ID del usuario que realiza la petición
     * @param templateDto el DTO de la plantilla
     * @return una ResponseEntity que contiene un TemplateDto
     * @throws InstanceNotFoundException si no se encuentra un usuario con el ID
     * proporcionado
     * @throws PermissionException si el ID del usuario que realiza la petición
     * no coincide con el ID del entrenador que crea el ciclo
     * @throws InvalidRoleException si el usuario que se va validar no tiene rol
     */
    @PostMapping("/create")
    public ResponseEntity<TemplateDto> createTemplate(@RequestAttribute Long userId,
            @Validated({TemplateDto.AllValidations.class}) @RequestBody TemplateDto templateDto)
            throws InstanceNotFoundException, PermissionException, InvalidRoleException {

        TrainingCycles cycle = cycleService.getCycleInfo(templateDto.getCycleId());

        validateTemplateUser(userId, cycle);

        Templates template = toTemplates(templateDto, cycle);

        templateService.createTemplate(template);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(template.getId()).toUri();

        return ResponseEntity.created(location).body(toTemplateDto(template));

    }

    /**
     * Añade una nueva fila a una plantilla de entrenamiento.
     *
     * @param userId el ID del usuario que realiza la petición
     * @param templateRowDto el DTO de la fila de una plantilla
     * @return una ResponseEntity que contiene un TemplateRowDto
     * @throws InstanceNotFoundException si no se encuentra un usuario con el ID
     * proporcionado
     * @throws PermissionException si el ID del usuario que realiza la petición
     * no coincide con el ID del entrenador que crea el ciclo
     * @throws InvalidRoleException si el usuario que se va validar no tiene rol
     */
    @PostMapping("/addRow")
    public ResponseEntity<TemplateRowDto> addRowToTemplate(@RequestAttribute Long userId,
            @Validated({TemplateRowDto.AllValidations.class}) @RequestBody TemplateRowDto templateRowDto)
            throws InstanceNotFoundException, PermissionException, InvalidRoleException {

        Exercises exercise = exerciseService.getExerciseInfo(templateRowDto.getExerciseId());
        Templates template = templateService.getTemplateInfo(templateRowDto.getTemplateId());

        validateTemplateUser(userId, template.getCycle());

        TemplateRows templateRow = toTemplateRows(templateRowDto, exercise, template);

        templateService.addTemplateRow(templateRow);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(templateRow.getId()).toUri();

        return ResponseEntity.created(location).body(toTemplateRowDto(templateRow));

    }

    /**
     * Devuelve la lista de plantillas de un ciclo.
     *
     * @param userId el ID del usuario que realiza la petición
     * @param cycleId el ID del ciclo
     * @return una lista de plantillas
     * @throws InstanceNotFoundException si no se encuentra un ciclo con el ID
     * proporcionado
     * @throws PermissionException si el ID del usuario que realiza la petición
     * no coincide con el ID del entrenador de los ciclos a obtener
     * @throws InvalidRoleException si el usuario que se va validar no tiene rol
     */
    @GetMapping("/fromCycle/{cycleId}")
    public List<TemplateDto> getTemplates(@RequestAttribute Long userId,
            @PathVariable("cycleId") Long cycleId) throws InstanceNotFoundException,
            PermissionException, InvalidRoleException {

        Users user = userService.loginFromId(userId);
        TrainingCycles cycle = cycleService.getCycleInfo(cycleId);

        if (user.getUserRole().toString().equals("CLIENT")) {
            validateTemplateUser(user.getTrainer().getId(), cycle);
        } else {
            validateTemplateUser(userId, cycle);
        }

        List<Templates> templates = templateService.getTemplates(cycleId);

        return toTemplatesDto(templates);

    }

    /**
     * Devuelve la plantilla de un entrenador a partir de su ID.
     *
     * @param userId el ID del usuario que realiza la petición
     * @param templateId el ID de la plantilla
     * @return una plantilla
     * @throws InstanceNotFoundException si no se encuentra una plantilla con el
     * ID proporcionado
     * @throws PermissionException si el ID del usuario que realiza la petición
     * no coincide con el ID del entrenador de los ciclos a obtener
     * @throws InvalidRoleException si el usuario que se va validar no tiene rol
     */
    @GetMapping("/template/{id}")
    public TemplateDto getTemplateInfo(@RequestAttribute Long userId,
            @PathVariable("id") Long templateId) throws InstanceNotFoundException,
            PermissionException, InvalidRoleException {

        Users user = userService.loginFromId(userId);
        Templates template = templateService.getTemplateInfo(templateId);

        if (user.getUserRole().toString().equals("CLIENT")) {
            validateTemplateUser(user.getTrainer().getId(), template.getCycle());
        } else {
            validateTemplateUser(userId, template.getCycle());
        }

        return toTemplateDto(template);

    }

    /**
     * Devuelve la lista de filas de una plantilla.
     *
     * @param userId el ID del usuario que realiza la petición
     * @param templateId el ID de la plantilla
     * @return una lista de filas
     * @throws InstanceNotFoundException si no se encuentra una plantilla con el
     * ID proporcionado
     * @throws PermissionException si el ID del usuario que realiza la petición
     * no coincide con el ID del entrenador de los ciclos a obtener
     * @throws InvalidRoleException si el usuario que se va validar no tiene rol
     */
    @GetMapping("/{templateId}/rows")
    public List<TemplateRowDto> getTemplateRows(@RequestAttribute Long userId,
            @PathVariable("templateId") Long templateId) throws InstanceNotFoundException,
            PermissionException, InvalidRoleException {

        Users user = userService.loginFromId(userId);
        Templates template = templateService.getTemplateInfo(templateId);

        if (user.getUserRole().toString().equals("CLIENT")) {
            validateTemplateUser(user.getTrainer().getId(), template.getCycle());
        } else {
            validateTemplateUser(userId, template.getCycle());
        }

        List<TemplateRows> templateRows = templateService.getTemplateRows(templateId);

        return toTemplateRowsDto(templateRows);

    }

    /**
     * Edita una plantilla.
     *
     * @param userId el ID del usuario que realiza la petición
     * @param id el ID de la plantilla que se va a editar
     * @param templateDto el template dto
     * @return el template dto
     * @throws InstanceNotFoundException si no se encuentra una plantilla con el
     * ID proporcionado
     * @throws PermissionException si el ID del usuario que realiza la petición
     * no coincide con el ID del usuario al que se le va a actualizar el perfil
     * @throws InvalidRoleException si el usuario que se va validar no tiene rol
     */
    @PutMapping("/template/{id}/update")
    public TemplateDto updateTemplate(@RequestAttribute Long userId, @PathVariable("id") Long id,
            @Validated({TemplateDto.UpdateValidations.class}) @RequestBody TemplateDto templateDto)
            throws InstanceNotFoundException, PermissionException, InvalidRoleException {

        TrainingCycles cycle = cycleService.getCycleInfo(templateDto.getCycleId());

        validateTemplateUser(userId, cycle);

        return toTemplateDto(templateService.updateTemplate(id, templateDto.getName()));

    }

    /**
     * Edita la fila de una plantilla.
     *
     * @param userId el ID del usuario que realiza la petición
     * @param id el ID de la fila que se va a editar
     * @param rowDto el template row dto
     * @return el template row dto
     * @throws InstanceNotFoundException si no se encuentra una fila con el ID
     * proporcionado
     * @throws PermissionException si el ID del usuario que realiza la petición
     * no coincide con el ID del usuario al que se le va a actualizar el perfil
     * @throws InvalidRoleException si el usuario que se va validar no tiene rol
     */
    @PutMapping("/templateRow/{id}")
    public TemplateRowDto updateTemplateRow(@RequestAttribute Long userId, @PathVariable("id") Long id,
            @Validated({TemplateRowDto.UpdateValidations.class}) @RequestBody TemplateRowDto rowDto)
            throws InstanceNotFoundException, PermissionException, InvalidRoleException {

        Exercises exercise = exerciseService.getExerciseInfo(rowDto.getExerciseId());
        Templates template = templateService.getTemplateInfo(rowDto.getTemplateId());

        validateTemplateUser(userId, template.getCycle());

        return toTemplateRowDto(templateService.updateTemplateRow(id, rowDto.getSeries(),
                rowDto.getRepetitions(), rowDto.getWeight(), exercise));

    }

    /**
     * Elimina una plantilla.
     *
     * @param userId el ID del usuario que realiza la petición
     * @param id el ID de la plantilla que se va a eliminar
     * @return el ID de la plantilla que ha sido eliminada
     * @throws InstanceNotFoundException si no se encuentra una plantilla con el
     * ID proporcionado
     * @throws PermissionException si el ID del usuario que realiza la petición
     * no coincide con el ID del ciclo que se va a eliminar
     * @throws InvalidRoleException si el usuario no tiene rol
     */
    @DeleteMapping("/{id}/delete")
    public Long deleteTemplate(@RequestAttribute Long userId, @PathVariable("id") Long id)
            throws InstanceNotFoundException, PermissionException, InvalidRoleException {

        Templates template = templateService.getTemplateInfo(id);

        validateTemplateUser(userId, template.getCycle());

        return templateService.deleteTemplate(id);

    }

    /**
     * Elimina la fila de una plantilla.
     *
     * @param userId el ID del usuario que realiza la petición
     * @param templateId el ID de la plantilla a la que pertenece la fila
     * @param id el ID de la fila que se va a eliminar
     * @return el ID de la fila que ha sido eliminada
     * @throws InstanceNotFoundException si no se encuentra una fila con el ID
     * proporcionado
     * @throws PermissionException si el ID del usuario que realiza la petición
     * no coincide con el ID del ciclo que se va a eliminar
     * @throws InvalidRoleException si el usuario no tiene rol
     */
    @DeleteMapping("/{templateId}/delete/{id}")
    public Long deleteCycle(@RequestAttribute Long userId,
            @PathVariable("templateId") Long templateId, @PathVariable("id") Long id)
            throws InstanceNotFoundException, PermissionException, InvalidRoleException {

        Templates template = templateService.getTemplateInfo(templateId);

        validateTemplateUser(userId, template.getCycle());

        return templateService.deleteTemplateRow(id);

    }

}
