package es.udc.fi.dc.tfg.model.services.exceptions;

/**
 * Clase IncorrectLoginException.
 */
@SuppressWarnings("serial")
public class IncorrectLoginException extends Exception {

    /**
     * El email.
     */
    private final String email;

    /**
     * La contraseña.
     */
    private final String password;

    /**
     * Instancia una nueva incorrect login exception.
     *
     * @param email el email
     * @param password la contraseña
     */
    public IncorrectLoginException(String email, String password) {

        this.email = email;
        this.password = password;

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
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

}
