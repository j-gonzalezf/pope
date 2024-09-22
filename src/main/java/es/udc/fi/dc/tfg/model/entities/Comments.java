package es.udc.fi.dc.tfg.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * Clase Comments que representa los comentarios en una plantilla.
 */
@Entity
@Table(name = "Comments")
public class Comments {

    /**
     * Identificador único autogenerado de la fila de una plantilla.
     */
    private Long id;

    /**
     * Contenido del comentario.
     */
    private String text;

    /**
     * Fecha en la que se realizó el comentario.
     */
    private LocalDateTime commentDate;

    /**
     * Plantilla asociada al comentario, puede ser nulo.
     */
    private Templates template;

    /**
     * Usuario que realizó el comentario.
     */
    private Users user;

    /**
     * Constructor vacío de la clase Comments.
     */
    public Comments() {
    }

    /**
     * Constructor de la clase Comments.
     *
     * @param text El contenido del comentario.
     * @param commentDate La fecha en la que se realizó el comentario.
     * @param template La plantilla asociada al comentario.
     * @param user El usuario que realizó el comentario.
     */
    public Comments(String text, LocalDateTime commentDate, Templates template, Users user) {

        this.text = text;
        this.commentDate = commentDate;
        this.template = template;
        this.user = user;

    }

    /**
     * Gets the sensation id.
     *
     * @return the sensation id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    /**
     * Sets the sensation id.
     *
     * @param id the new sensation id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the text.
     *
     * @return the text
     */
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
    public LocalDateTime getCommentDate() {
        return commentDate;
    }

    /**
     * Sets the comment date.
     *
     * @param commentDate the new comment date
     */
    public void setCommentDate(LocalDateTime commentDate) {
        this.commentDate = commentDate;
    }

    /**
     * Gets the template.
     *
     * @return the template
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "templateId")
    public Templates getTemplate() {
        return template;
    }

    /**
     * Sets the template.
     *
     * @param template the new template
     */
    public void setTemplate(Templates template) {
        this.template = template;
    }

    /**
     * Gets the user.
     *
     * @return the user
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    public Users getUser() {
        return user;
    }

    /**
     * Sets the user.
     *
     * @param user the new user
     */
    public void setUser(Users user) {
        this.user = user;
    }

}
