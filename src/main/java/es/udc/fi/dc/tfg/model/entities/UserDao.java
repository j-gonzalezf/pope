package es.udc.fi.dc.tfg.model.entities;

import java.util.List;
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
    
    /**
     * Devuelve una lista con los clientes de un entrenador.
     * 
     * @param trainerId El ID del entrenador.
     * @return La lista de objetos Users que representa los clientes.
     */
    List<Users> findByTrainerId(Long trainerId);

}
