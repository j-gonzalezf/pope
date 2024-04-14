package es.udc.fi.dc.tfg.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.tfg.model.common.exceptions.DuplicateInstanceException;
import es.udc.fi.dc.tfg.model.entities.Users;
import es.udc.fi.dc.tfg.model.entities.UserDao;

/**
 * Clase UserServiceImpl.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    /**
     * El password encoder.
     */
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * El user dao.
     */
    @Autowired
    private UserDao userDao;

    /**
     * Crea un nuevo usuario.
     *
     * @param user el objeto Users que representa al usuario a crear.
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email.
     */
    public void signUp(Users user) throws DuplicateInstanceException {

        if (userDao.existsByEmail(user.getEmail())) {
            throw new DuplicateInstanceException("project.entities.user", user.getEmail());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userDao.save(user);

    }

}
