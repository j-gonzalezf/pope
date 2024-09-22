package es.udc.fi.dc.tfg.rest.dtos;

import es.udc.fi.dc.tfg.model.entities.Comments;
import es.udc.fi.dc.tfg.model.entities.Templates;
import es.udc.fi.dc.tfg.model.entities.Users;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase CommentConversor.
 */
public class CommentConversor {

    /**
     * Instancia un nuevo comment conversor
     */
    private CommentConversor() {
    }

    /**
     * Convierte un objeto Comments a un objeto CommentDto
     *
     * @param comment el objeto Comments a convertir
     * @return un nuevo objeto CommentDto que contiene los mismos datos que el
     * objeto Comments proporcionado
     */
    public static final CommentDto toCommentDto(Comments comment) {
        return new CommentDto(comment.getId(), comment.getText(),
                comment.getCommentDate().toString(),
                comment.getTemplate().getId(), comment.getUser().getId());
    }

    /**
     * Convierte una lista de objetos Comments a objetos CommentDto.
     *
     * @param comments la lista de objetos Comments a convertir
     * @return una nueva lista de objetos CommentDto que contiene los mismos
     * datos que la lista de objetos Comments proporcionada
     */
    public static final List<CommentDto> toCommentsDto(List<Comments> comments) {
        return comments.stream().map(comment -> {
            return toCommentDto(comment);
        }).collect(Collectors.toList());
    }

    /**
     * Convierte un objeto CommentDto a un objeto Comments
     *
     * @param commentDto el objeto CommentDto a convertir
     * @param template la plantilla CommentDto
     * @param user el usuario del CommentDto
     * @return un nuevo objeto Comments que contiene los mismos datos que el
     * objeto CommentDto proporcionado
     */
    public static final Comments toComments(
            CommentDto commentDto, Templates template, Users user) {
        return new Comments(commentDto.getText(),
                LocalDateTime.parse(commentDto.getCommentDate()),
                template, user);
    }

}
