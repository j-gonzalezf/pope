package es.udc.fi.dc.tfg.model.services;

import es.udc.fi.dc.tfg.model.common.exceptions.DuplicateInstanceException;
import es.udc.fi.dc.tfg.model.entities.Users;

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

}
