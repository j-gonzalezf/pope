package es.udc.fi.dc.tfg.rest.controllers;

import static es.udc.fi.dc.tfg.rest.dtos.CommentConversor.toCommentDto;
import static es.udc.fi.dc.tfg.rest.dtos.CommentConversor.toCommentsDto;
import static es.udc.fi.dc.tfg.rest.dtos.CommentConversor.toComments;

import es.udc.fi.dc.tfg.model.common.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.tfg.model.entities.Comments;
import es.udc.fi.dc.tfg.model.entities.Templates;
import es.udc.fi.dc.tfg.model.entities.TrainingCycles;
import es.udc.fi.dc.tfg.model.entities.Users;
import es.udc.fi.dc.tfg.model.services.CommentService;
import es.udc.fi.dc.tfg.model.services.TemplateService;
import es.udc.fi.dc.tfg.model.services.TrainingCycleService;
import es.udc.fi.dc.tfg.model.services.UserService;
import es.udc.fi.dc.tfg.model.services.exceptions.InvalidRoleException;
import es.udc.fi.dc.tfg.model.services.exceptions.PermissionException;
import es.udc.fi.dc.tfg.rest.dtos.CommentDto;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Clase CommentController.
 */
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private TemplateService templateService;

    @Autowired
    private TrainingCycleService cycleService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    // Método para validar un comentario
    private void validateCommentUser(Long userId, Long clientId, TrainingCycles cycle)
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
     * Guarda un nuevo comentario.
     *
     * @param userId el ID del usuario que realiza la petición
     * @param commentDto el DTO del comentario
     * @return una ResponseEntity que contiene un CommentDto
     * @throws InstanceNotFoundException si no se encuentra un usuario con el ID
     * proporcionado
     * @throws PermissionException si el ID del usuario que realiza la petición
     * no coincide con el ID del entrenador que crea el ciclo
     * @throws InvalidRoleException si el usuario que se va validar no tiene rol
     */
    @PostMapping("/write")
    public ResponseEntity<CommentDto> writeComment(@RequestAttribute Long userId,
            @Validated({CommentDto.AllValidations.class}) @RequestBody CommentDto commentDto)
            throws InstanceNotFoundException, PermissionException, InvalidRoleException {

        Users user = userService.loginFromId(userId);
        Templates template = null;

        if (commentDto.getTemplateId() != null) {
            template = templateService.getTemplateInfo(commentDto.getTemplateId());
            TrainingCycles cycle = cycleService.getCycleInfo(template.getCycle().getId());

            validateCommentUser(user.getId(), commentDto.getUserId(), cycle);
        } else {
            userService.validateUser(userId, commentDto.getUserId());
        }

        Comments comment = toComments(commentDto, template, user);

        commentService.writeComment(comment);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(comment.getId()).toUri();

        return ResponseEntity.created(location).body(toCommentDto(comment));

    }

    /**
     * Devuelve la lista de comentarios de una plantilla.
     *
     * @param userId el ID del usuario que realiza la petición
     * @param templateId el ID de la plantilla
     * @return una lista de comentarios
     * @throws InstanceNotFoundException si no se encuentra una plantilla con el
     * ID proporcionado
     * @throws PermissionException si el ID del usuario que realiza la petición
     * no coincide con el ID del entrenador de los ciclos a obtener
     * @throws InvalidRoleException si el usuario que se va validar no tiene rol
     */
    @GetMapping("/fromTemplate/{templateId}")
    public List<CommentDto> getTemplateComments(@RequestAttribute Long userId,
            @PathVariable("templateId") Long templateId)
            throws InstanceNotFoundException, PermissionException, InvalidRoleException {

        Templates template = templateService.getTemplateInfo(templateId);
        TrainingCycles cycle = cycleService.getCycleInfo(template.getCycle().getId());

        userService.validateUser(userId, cycle.getClient().getId());

        List<Comments> comments = commentService.getTemplateComments(templateId);

        return toCommentsDto(comments);

    }

}
