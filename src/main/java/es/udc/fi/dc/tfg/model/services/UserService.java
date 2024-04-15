package es.udc.fi.dc.tfg.model.services;

import es.udc.fi.dc.tfg.model.common.exceptions.DuplicateInstanceException;
import es.udc.fi.dc.tfg.model.common.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.tfg.model.entities.Users;
import es.udc.fi.dc.tfg.model.services.exceptions.IncorrectLoginException;
import es.udc.fi.dc.tfg.model.services.exceptions.IncorrectPasswordException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Interfaz UserService.
 */
public interface UserService {

    /**
     * Crea un nuevo usuario.
     *
     * @param user el objeto Users que representa al usuario a crear.
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email.
     */
    void signUp(Users user) throws DuplicateInstanceException;

    /**
     * Inicia sesión con el email y la contraseña proporcionados.
     *
     * @param email El correo electrónico del usuario.
     * @param password La contraseña del usuario.
     * @return El objeto Users que representa al usuario que inició sesión.
     * @throws IncorrectLoginException si el email o la contraseña son
     * incorrectos.
     */
    Users login(String email, String password) throws IncorrectLoginException;

    /**
     * Inicia sesión a partir del id del usuario.
     *
     * @param id El ID del usuario.
     * @return El objeto Users que representa al usuario con el perfil
     * actualizado.
     * @throws InstanceNotFoundException si no se encuentra un usuario con el ID
     * proporcionado.
     */
    Users loginFromId(Long id) throws InstanceNotFoundException;

    /**
     * Actualiza el perfil de un entrenador.
     *
     * @param id El ID del entrenador.
     * @param email El nuevo correo electrónico del entrenador.
     * @param fullName El nuevo nombre completo del entrenador.
     * @param phone El nuevo número de teléfono del entrenador.
     * @param icon La foto de perfil del entrenador.
     * @param socialLinks El enlace a las redes sociales del entrenador.
     * @return El objeto Users que representa al usuario con el perfil
     * actualizado.
     * @throws InstanceNotFoundException si no se encuentra un usuario con el ID
     * proporcionado.
     */
    Users updateProfile(Long id, String email, String fullName, String phone,
            String icon, String socialLinks) throws InstanceNotFoundException;

    /**
     * Actualiza el perfil de un cliente.
     *
     * @param id El ID del cliente.
     * @param email El nuevo correo electrónico del cliente.
     * @param fullName El nuevo nombre completo del cliente.
     * @param phone El nuevo número de teléfono del cliente.
     * @param icon La foto de perfil del cliente.
     * @param birthdate La fecha de nacimiento del cliente.
     * @param injuries Las lesiones del cliente.
     * @param goals Los objetivos del cliente.
     * @param height La altura del cliente en cm.
     * @return El objeto Users que representa al usuario con el perfil
     * actualizado.
     * @throws InstanceNotFoundException si no se encuentra un usuario con el ID
     * proporcionado.
     */
    public Users updateClient(Long id, String email, String fullName, String phone, String icon,
            LocalDateTime birthdate, String injuries, String goals, BigDecimal height)
            throws InstanceNotFoundException;

    /**
     * Cambia la contraseña de un usuario.
     *
     * @param id El ID del usuario.
     * @param oldPassword La contraseña antigua del usuario.
     * @param newPassword La nueva contraseña del usuario.
     * @throws InstanceNotFoundException si no se encuentra un usuario con el ID
     * proporcionado.
     * @throws IncorrectPasswordException si la contraseña antigua proporcionada
     * no coincide con la contraseña actual del usuario.
     */
    void changePassword(Long id, String oldPassword, String newPassword)
            throws InstanceNotFoundException, IncorrectPasswordException;

}
