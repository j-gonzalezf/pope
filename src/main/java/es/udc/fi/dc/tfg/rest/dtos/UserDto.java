package es.udc.fi.dc.tfg.rest.dtos;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import org.hibernate.validator.constraints.URL;

/**
 * Clase UserDto.
 */
public class UserDto {

    /**
     * La interfaz AllValidations.
     */
    public interface AllValidations {
    }

    /**
     * La interfaz UpdateValidations.
     */
    public interface UpdateValidations {
    }

    /**
     * Identificador del usuario.
     */
    private Long id;

    /**
     * Email del usuario para el inicio de sesión.
     */
    private String email;

    /**
     * Clave de acceso del usuario.
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
     * Rol del usuario.
     */
    private String role;

    /**
     * Enlace a las redes sociales del entrenador, puede ser nulo.
     */
    private String socialLinks;

    /**
     * Fecha de nacimiento del cliente, puede ser nulo.
     */
    private String birthdate;

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
     * Identificador del entrenador que creó al cliente.
     */
    private Long trainerId;

    /**
     * Instantiates a new user dto.
     */
    public UserDto() {
    }

    /**
     * Constructor para usuarios de la clase UserDto.
     *
     * @param id El identificador del usuario.
     * @param email El email del usuario.
     * @param fullName El nombre completo del usuario.
     * @param phone El teléfono del usuario.
     * @param icon La foto de perfil del usuario.
     * @param role El rol del usuario.
     * @param socialLinks El enlace a las redes sociales del usuario.
     * @param birthdate La fecha de nacimiento del usuario.
     * @param injuries Las lesiones del usuario.
     * @param goals Los objetivos del usuario.
     * @param height La altura del usuario en cm.
     * @param trainerId El ID del entrenador del usuario.
     */
    public UserDto(Long id, String email, String fullName, String phone,
            String icon, String role, String socialLinks, String birthdate,
            String injuries, String goals, BigDecimal height, Long trainerId) {

        this.id = id;
        this.email = email != null ? email.trim() : null;
        this.fullName = fullName != null ? fullName.trim() : null;
        this.phone = phone != null ? phone.trim() : null;
        this.icon = icon;
        this.role = role;
        this.socialLinks = socialLinks != null ? socialLinks.trim() : null;
        this.birthdate = birthdate;
        this.injuries = injuries != null ? injuries.trim() : null;
        this.goals = goals != null ? goals.trim() : null;
        this.height = height;
        this.trainerId = trainerId;

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
     * Gets the email.
     *
     * @return the email
     */
    @NotNull(groups = {AllValidations.class, UpdateValidations.class})
    @Size(min = 1, max = 60, groups = {AllValidations.class, UpdateValidations.class})
    @Email(groups = {AllValidations.class, UpdateValidations.class})
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email.
     *
     * @param email the new email
     */
    public void setEmail(String email) {

        if (email != null) {
            this.email = email.trim();
        } else {
            this.email = null;
        }

    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    @NotNull(groups = {AllValidations.class})
    @Size(min = 1, max = 255, groups = {AllValidations.class})
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
    @NotNull(groups = {AllValidations.class, UpdateValidations.class})
    @Size(min = 1, max = 60, groups = {AllValidations.class, UpdateValidations.class})
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the full name.
     *
     * @param fullName the new full name
     */
    public void setFullName(String fullName) {

        if (fullName != null) {
            this.fullName = fullName.trim();
        } else {
            this.fullName = null;
        }

    }

    /**
     * Gets the phone.
     *
     * @return the phone
     */
    @Size(max = 15, groups = {AllValidations.class, UpdateValidations.class})
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the phone.
     *
     * @param phone the new phone
     */
    public void setPhone(String phone) {

        if (phone != null) {
            this.phone = phone.trim();
        } else {
            this.phone = null;
        }

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
    public String getRole() {
        return role;
    }

    /**
     * Sets the role.
     *
     * @param role the new role
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Gets the social links.
     *
     * @return the social links
     */
    @URL(groups = {AllValidations.class, UpdateValidations.class})
    public String getSocialLinks() {
        return socialLinks;
    }

    /**
     * Sets the social links.
     *
     * @param socialLinks the new social links
     */
    public void setSocialLinks(String socialLinks) {

        if (socialLinks != null) {
            this.socialLinks = socialLinks.trim();
        } else {
            this.socialLinks = null;
        }

    }

    /**
     * Gets the birthdate.
     *
     * @return the birthdate
     */
    //@Past(groups = {AllValidations.class, UpdateValidations.class})
    public String getBirthdate() {
        return birthdate;
    }

    /**
     * Sets the birthdate.
     *
     * @param birthdate the new birthdate
     */
    public void setBirthdate(String birthdate) {
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

        if (injuries != null) {
            this.injuries = injuries.trim();
        } else {
            this.injuries = null;
        }

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
     * Sets the social goals.
     *
     * @param goals the new goals
     */
    public void setGoals(String goals) {

        if (goals != null) {
            this.goals = goals.trim();
        } else {
            this.goals = null;
        }

    }

    /**
     * Gets the height.
     *
     * @return the height
     */
    @DecimalMin(value = "0.00", groups = {AllValidations.class, UpdateValidations.class})
    @DecimalMax(value = "999.99", groups = {AllValidations.class, UpdateValidations.class})
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

    /**
     * Gets the trainerId.
     *
     * @return the trainerId
     */
    public Long getTrainerId() {
        return trainerId;
    }

    /**
     * Sets the trainerId.
     *
     * @param trainerId the new trainerId
     */
    public void setTrainerId(Long trainerId) {
        this.trainerId = trainerId;
    }

}
