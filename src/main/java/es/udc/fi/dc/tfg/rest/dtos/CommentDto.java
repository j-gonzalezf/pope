package es.udc.fi.dc.tfg.rest.dtos;

import jakarta.validation.constraints.NotNull;

/**
 * Clase CommentDto.
 */
public class CommentDto {

    /**
     * La interfaz AllValidations.
     */
    public interface AllValidations {
    }

    /**
     * Identificador del comentario.
     */
    private Long id;

    /**
     * Contenido del comentario.
     */
    private String text;

    /**
     * Fecha en la que se realizó el comentario.
     */
    private String commentDate;

    /**
     * Identificador de la plantilla asociada al comentario, puede ser nulo.
     */
    private Long templateId;

    /**
     * Identificador del usuario que realizó el comentario.
     */
    private Long userId;

    /**
     * Instantiates a new comment dto.
     */
    public CommentDto() {
    }

    /**
     * Constructor de la clase CommentDto.
     *
     * @param id El identificador de las sensaciones.
     * @param text El contenido del comentario.
     * @param commentDate La fecha en la que se realizó el comentario.
     * @param templateId El ID de la plantilla asociada al comentario.
     * @param userId El ID del usuario que realizó el comentario.
     */
    public CommentDto(Long id, String text, String commentDate, Long templateId, Long userId) {

        this.id = id;
        this.text = text;
        this.commentDate = commentDate;
        this.templateId = templateId;
        this.userId = userId;

    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the text
     *
     * @return the text
     */
    @NotNull(groups = {CommentDto.AllValidations.class})
    public String getText() {
        return text;
    }

    /**
     * Sets the text.
     *
     * @param text the new text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Gets the comment date.
     *
     * @return the comment date
     */
    @NotNull(groups = {CommentDto.AllValidations.class})
    public String getCommentDate() {
        return commentDate;
    }

    /**
     * Sets the comment date.
     *
     * @param commentDate the new comment date
     */
    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    /**
     * Gets the templateId.
     *
     * @return the templateId
     */
    public Long getTemplateId() {
        return templateId;
    }

    /**
     * Sets the templateId.
     *
     * @param templateId the new templateId
     */
    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    /**
     * Gets the userId.
     *
     * @return the userId
     */
    @NotNull(groups = {CommentDto.AllValidations.class})
    public Long getUserId() {
        return userId;
    }

    /**
     * Sets the userId.
     *
     * @param userId the new userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
