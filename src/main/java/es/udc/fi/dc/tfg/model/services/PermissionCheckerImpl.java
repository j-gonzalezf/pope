package es.udc.fi.dc.tfg.model.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.tfg.model.common.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.tfg.model.entities.Users;
import es.udc.fi.dc.tfg.model.entities.UserDao;

/**
 * Clase PermissionCheckerImpl.
 */
@Service
@Transactional(readOnly = true)
public class PermissionCheckerImpl implements PermissionChecker {

    /**
     * El user dao.
     *
     * @param userId el user id
     * @return el user
     * @throws InstanceNotFoundException si no se encuentra un usuario con el ID
     * proporcionado
     */
    @Autowired
    private UserDao userDao;

    @Override
    public void checkUserExists(Long userId) throws InstanceNotFoundException {

        if (!userDao.existsById(userId)) {
            throw new InstanceNotFoundException("project.entities.user", userId);
        }

    }

    @Override
    public Users checkUser(Long userId) throws InstanceNotFoundException {

        Optional<Users> user = userDao.findById(userId);

        if (!user.isPresent()) {
            throw new InstanceNotFoundException("project.entities.user", userId);
        }

        return user.get();

    }

}
