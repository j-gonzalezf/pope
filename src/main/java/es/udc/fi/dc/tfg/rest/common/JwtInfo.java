package es.udc.fi.dc.tfg.rest.common;

/**
 * Clase JwtInfo.
 */
public class JwtInfo {

    /**
     * El user id.
     */
    private Long userId;

    /**
     * El email.
     */
    private String email;

    /**
     * El role.
     */
    private String role;

    /**
     * Instancia un nuevo jwt info.
     *
     * @param userId el user id del usuario
     * @param email el email del usuario
     * @param role el role del usuario
     */
    public JwtInfo(Long userId, String email, String role) {

        this.userId = userId;
        this.email = email;
        this.role = role;

    }

    /**
     * Gets the user id.
     *
     * @return the user id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Sets the user id.
     *
     * @param userId the new user id
     */
    public void setUserId(Long userId) {
        this.userId = userId;
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
    public void setUserName(String email) {
        this.email = email;
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

}
