package es.udc.fi.dc.tfg.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.tfg.model.common.exceptions.DuplicateInstanceException;
import es.udc.fi.dc.tfg.model.common.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.tfg.model.entities.Users;
import es.udc.fi.dc.tfg.model.entities.UserDao;
import es.udc.fi.dc.tfg.model.services.exceptions.IncorrectLoginException;
import es.udc.fi.dc.tfg.model.services.exceptions.IncorrectPasswordException;
import es.udc.fi.dc.tfg.model.services.exceptions.InvalidRoleException;
import es.udc.fi.dc.tfg.model.services.exceptions.PermissionException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Clase UserServiceImpl.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    /**
     * El permission checker.
     */
    @Autowired
    private PermissionChecker permissionChecker;

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

    // Método para validar un entrenador cuando id es un TRAINER
    private void validateForTrainer(Long userId, Long id) throws PermissionException {
        if (!id.equals(userId)) {
            throw new PermissionException();
        }
    }

    // Método para validar un entrenador cuando id es un CLIENT
    private void validateForClient(Long userId, Users user) throws PermissionException {
        if (!user.getTrainer().getId().equals(userId)) {
            throw new PermissionException();
        }
    }

    /**
     * Valida un usuario.
     *
     * @param userId el ID del usuario que realiza la petición.
     * @param id el ID del usuario al que se va a realizar una acción CRUD.
     * @throws InstanceNotFoundException si no se encuentra un usuario con el ID
     * proporcionado.
     * @throws PermissionException si el usuario que realiza la petición no
     * tiene permiso para realizar la acción.
     * @throws InvalidRoleException si el usuario que se va validar no tiene rol
     */
    @Override
    public void validateUser(Long userId, Long id)
            throws InstanceNotFoundException, PermissionException, InvalidRoleException {

        permissionChecker.checkUserExists(id);

        Users user = loginFromId(id);
        String role = user.getUserRole().toString();

        switch (role) {
            // En caso de CRUD a un trainer, comprobamos que el que realiza la
            // petición y el trainer a actualizar son el mismo user
            case "TRAINER" ->
                validateForTrainer(userId, id);
            // En caso de CRUD a un client, comprobamos si el que realiza la
            // petición y el trainer del client a actualizar son el mismo user
            case "CLIENT" ->
                validateForClient(userId, user);

            default ->
                throw new InvalidRoleException();
        }
    }

    /**
     * Crea un nuevo usuario.
     *
     * @param user el objeto Users que representa al usuario a crear.
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email.
     */
    @Override
    public void signUp(Users user) throws DuplicateInstanceException {

        if (userDao.existsByEmail(user.getEmail())) {
            throw new DuplicateInstanceException("project.entities.user", user.getEmail());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userDao.save(user);

    }

    /**
     * Inicia sesión con el email y la contraseña proporcionados.
     *
     * @param email El correo electrónico del usuario.
     * @param password La contraseña del usuario.
     * @return El objeto Users que representa al usuario que inició sesión.
     * @throws IncorrectLoginException si el email o la contraseña son
     * incorrectos.
     */
    @Override
    @Transactional(readOnly = true)
    public Users login(String email, String password) throws IncorrectLoginException {

        Optional<Users> user = userDao.findByEmail(email);

        if (!user.isPresent()) {
            throw new IncorrectLoginException(email, password);
        }

        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            throw new IncorrectLoginException(email, password);
        }

        return user.get();

    }

    /**
     * Inicia sesión a partir del id del usuario.
     *
     * @param id El ID del usuario.
     * @return El objeto Users que representa al usuario con el perfil
     * actualizado.
     * @throws InstanceNotFoundException si no se encuentra un usuario con el ID
     * proporcionado.
     */
    @Override
    @Transactional(readOnly = true)
    public Users loginFromId(Long id) throws InstanceNotFoundException {
        return permissionChecker.checkUser(id);
    }

    /**
     * Devuelve una lista con los clientes de un entrenador.
     *
     * @param trainerId El ID del entrenador.
     * @return La lista de objetos Users que representa los clientes.
     */
    @Override
    public List<Users> getClients(Long trainerId) {

        List<Users> clients = userDao.findByTrainerId(trainerId);
        return clients;

    }

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
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email.
     * @throws InstanceNotFoundException si no se encuentra un usuario con el ID
     * proporcionado.
     */
    @Override
    public Users updateProfile(Long id, String email, String fullName, String phone,
            String icon, String socialLinks) throws DuplicateInstanceException, InstanceNotFoundException {

        Users user = permissionChecker.checkUser(id);

        if (!user.getEmail().equals(email) && userDao.existsByEmail(email)) {
            throw new DuplicateInstanceException("project.entities.user", email);
        }

        user.setEmail(email);
        user.setFullName(fullName);
        user.setPhone(phone);
        user.setIcon(icon);
        user.setSocialLinks(socialLinks);

        return user;

    }

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
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email.
     * @throws InstanceNotFoundException si no se encuentra un usuario con el ID
     * proporcionado.
     */
    @Override
    public Users updateClient(Long id, String email, String fullName, String phone, String icon,
            LocalDate birthdate, String injuries, String goals, BigDecimal height)
            throws DuplicateInstanceException, InstanceNotFoundException {

        Users user = permissionChecker.checkUser(id);

        if (!user.getEmail().equals(email) && userDao.existsByEmail(email)) {
            throw new DuplicateInstanceException("project.entities.user", email);
        }

        user.setEmail(email);
        user.setFullName(fullName);
        user.setPhone(phone);
        user.setIcon(icon);
        user.setBirthdate(birthdate);
        user.setInjuries(injuries);
        user.setGoals(goals);
        user.setHeight(height);

        return user;

    }

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
    @Override
    public void changePassword(Long id, String oldPassword, String newPassword)
            throws InstanceNotFoundException, IncorrectPasswordException {

        Users user = permissionChecker.checkUser(id);

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IncorrectPasswordException();
        } else {
            user.setPassword(passwordEncoder.encode(newPassword));
        }

    }

    /**
     * Elimina la cuenta de un usuario.
     *
     * @param id El ID del usuario.
     * @return El ID del usuario que ha sido eliminado.
     * @throws InstanceNotFoundException si no se encuentra un usuario con el ID
     * proporcionado.
     */
    @Override
    public Long deleteUser(Long id) throws InstanceNotFoundException {

        permissionChecker.checkUserExists(id);

        userDao.deleteById(id);

        return id;

    }

}
