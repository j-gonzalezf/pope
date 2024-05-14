package es.udc.fi.dc.tfg.model.services;

import es.udc.fi.dc.tfg.model.common.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.tfg.model.entities.Users;

/**
 * Interfaz PermissionChecker.
 */
public interface PermissionChecker {

    /**
     * Comprueba si existe un usuario con el ID proporcionado.
     *
     * @param userId El ID del usuario a comprobar
     * @throws InstanceNotFoundException si no se encuentra un usuario con el ID
     * proporcionado
     */
    public void checkUserExists(Long userId) throws InstanceNotFoundException;

    /**
     * Comprueba y devuelve el usuario con el ID proporcionado.
     *
     * @param userId El ID del usuario a comprobar
     * @return El usuario con el ID proporcionado
     * @throws InstanceNotFoundException si no se encuentra un usuario con el ID
     * proporcionado
     */
    public Users checkUser(Long userId) throws InstanceNotFoundException;

}
