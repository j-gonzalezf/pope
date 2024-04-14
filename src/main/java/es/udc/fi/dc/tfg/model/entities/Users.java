package es.udc.fi.dc.tfg.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Clase User que representa a los usuarios de la aplicación.
 */
@Entity
public class Users {

    /**
     * Enumeración RoleType que representa los roles de los usuarios.
     */
    public enum RoleType {
        TRAINER,
        CLIENT
    }

    /**
     * Identificador único autogenerado del usuario.
     */
    private Long id;

    /**
     * Email del usuario para el inicio de sesión.
     */
    private String email;

    /**
     * Clave de acceso del usuario para el inicio de sesión.
     */
    private String password;

    /**
     * Nombre completo del usuario.
     */
    private String fullName;

    /**
     * Teléfono del usuario, puede ser nulo.
     */
    private String phone;

    /**
     * Foto de perfil del usuario, puede ser nulo.
     */
    private String icon;

    /**
     * Rol del usuario. Puede ser TRAINER (entrenador) o CLIENT (cliente).
     */
    private RoleType role;

    /**
     * Enlace a las redes sociales del entrenador, puede ser nulo.
     */
    private String socialLinks;

    /**
     * Fecha de nacimiento del cliente, puede ser nulo.
     */
    private LocalDateTime birthdate;

    /**
     * Descripción sobre las lesiones o impedimentos del cliente, puede ser
     * nulo.
     */
    private String injuries;

    /**
     * Descripción sobre los objetivos del cliente, puede ser nulo.
     */
    private String goals;

    /**
     * Altura del cliente en cm, puede ser nulo.
     */
    private BigDecimal height;

    /**
     * Constructor vacío de la clase Users.
     */
    public Users() {
    }

    /**
     * Constructor para entrenadores de la clase Users.
     *
     * @param email El email del usuario.
     * @param password La contraseña del usuario.
     * @param fullName El nombre completo del usuario.
     * @param phone El teléfono del usuario.
     * @param icon La foto de perfil del usuario.
     * @param socialLinks El enlace a las redes sociales del usuario.
     */
    public Users(String email, String password, String fullName,
            String phone, String icon, String socialLinks) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.icon = icon;
        this.role = Users.RoleType.TRAINER;
        this.socialLinks = socialLinks;
    }

    /**
     * Constructor para clientes de la clase Users.
     *
     * @param email El email del usuario.
     * @param password La contraseña del usuario.
     * @param fullName El nombre completo del usuario.
     * @param phone El teléfono del usuario.
     * @param icon La foto de perfil del usuario.
     * @param birthdate La fecha de nacimiento del usuario.
     * @param injuries Las lesiones del usuario.
     * @param goals Los objetivos del usuario.
     * @param height La altura del usuario en cm.
     */
    public Users(String email, String password, String fullName,
            String phone, String icon, LocalDateTime birthdate,
            String injuries, String goals, BigDecimal height) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.icon = icon;
        this.role = Users.RoleType.CLIENT;
        this.birthdate = birthdate;
        this.injuries = injuries;
        this.goals = goals;
        this.height = height;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
     * Gets the email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email.
     *
     * @param email the new email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the full name.
     *
     * @return the full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the full name.
     *
     * @param fullName the new full name
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Gets the phone.
     *
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the phone.
     *
     * @param phone the new phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets the icon.
     *
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Sets the icon.
     *
     * @param icon the new icon
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * Gets the role.
     *
     * @return the role
     */
    public RoleType getRole() {
        return role;
    }

    /**
     * Sets the role.
     *
     * @param role the new role
     */
    public void setRole(RoleType role) {
        if (this.role == null) {
            this.role = role;
        }
    }

    /**
     * Gets the social links.
     *
     * @return the social links
     */
    public String getSocialLinks() {
        return socialLinks;
    }

    /**
     * Sets the social links.
     *
     * @param socialLinks the new social links
     */
    public void setSocialLinks(String socialLinks) {
        this.socialLinks = socialLinks;
    }

    /**
     * Gets the birthdate.
     *
     * @return the birthdate
     */
    public LocalDateTime getBirthdate() {
        return birthdate;
    }

    /**
     * Sets the birthdate.
     *
     * @param birthdate the new birthdate
     */
    public void setBirthdate(LocalDateTime birthdate) {
        this.birthdate = birthdate;
    }

    /**
     * Gets the injuries.
     *
     * @return the injuries
     */
    public String getInjuries() {
        return injuries;
    }

    /**
     * Sets the injuries.
     *
     * @param injuries the new injuries
     */
    public void setInjuries(String injuries) {
        this.injuries = injuries;
    }

    /**
     * Gets the goals.
     *
     * @return the goals
     */
    public String getGoals() {
        return goals;
    }

    /**
     * Sets the goals.
     *
     * @param goals the new goals
     */
    public void setGoals(String goals) {
        this.goals = goals;
    }

    /**
     * Gets the height.
     *
     * @return the height
     */
    public BigDecimal getHeight() {
        return height;
    }

    /**
     * Sets the height.
     *
     * @param height the new height
     */
    public void setHeight(BigDecimal height) {
        this.height = height;
    }

}
