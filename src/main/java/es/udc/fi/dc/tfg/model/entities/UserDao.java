package es.udc.fi.dc.tfg.model.entities;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interfaz UserDao.
 */
public interface UserDao extends JpaRepository<Users, Long> {

    /**
     * Comprueba si existe un usuario con el email proporcionado.
     *
     * @param email El email del usuario a comprobar
     * @return true si existe un usuario con el email proporcionado, false en
     * caso contrario
     */
    boolean existsByEmail(String email);

    /**
     * Comprueba si existe un usuario con el email proporcionado.
     *
     * @param email El email del usuario a comprobar
     * @return true si existe un usuario con el email proporcionado, false en
     * caso contrario
     */
    Optional<Users> findByEmail(String email);

}
