package es.udc.fi.dc.tfg.rest.dtos;

import jakarta.validation.constraints.NotNull;

/**
 * Clase LoginParamsDto.
 */
public class LoginParamsDto {

    /**
     * El email.
     */
    private String email;

    /**
     * La contraseña.
     */
    private String password;

    /**
     * Instancia un nuevo login params dto.
     */
    public LoginParamsDto() {
        super();
    }

    /**
     * Gets the email.
     *
     * @return the email
     */
    @NotNull
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email.
     *
     * @param email the new email
     */
    public void setEmail(String email) {
        this.email = email.trim();
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    @NotNull
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

}
