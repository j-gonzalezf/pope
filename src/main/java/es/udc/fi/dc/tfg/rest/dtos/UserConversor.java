package es.udc.fi.dc.tfg.rest.dtos;

import es.udc.fi.dc.tfg.model.entities.Users;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase UserConversor.
 */
public class UserConversor {

    /**
     * Instancia un nuevo user conversor.
     */
    private UserConversor() {
    }

    /**
     * Convierte un objeto Users a un objeto UserDto.
     *
     * @param user el objeto Users a convertir
     * @return un nuevo objeto UserDto que contiene los mismos datos que el
     * objeto Users proporcionado
     */
    public static final UserDto toUserDto(Users user) {

        Long trainerId = null;
        String birthdate = null;

        if (user.getTrainer() != null) {
            trainerId = user.getTrainer().getId();
        }

        if (user.getBirthdate() != null) {
            birthdate = user.getBirthdate().toString();
        }

        return new UserDto(user.getId(), user.getEmail(), user.getFullName(),
                user.getPhone(), user.getIcon(), user.getUserRole().toString(),
                user.getSocialLinks(), birthdate, user.getInjuries(),
                user.getGoals(), user.getHeight(), trainerId);

    }

    /**
     * Convierte una lista de objetos Users a objetos UserDto.
     *
     * @param clients la lista de objetos Users a convertir
     * @return una nueva lista de objetos UserDto que contiene los mismos datos
     * que la lista de objetos Users proporcionada
     */
    public static final List<UserDto> toUsersDto(List<Users> clients) {
        return clients.stream().map(client -> {
            return toUserDto(client);
        }).collect(Collectors.toList());
    }

    /**
     * Convierte un objeto UserDto a un objeto Users.
     *
     * @param userDto el objeto UserDto a convertir
     * @param trainer el entrenador del UserDto
     * @return un nuevo objeto Users que contiene los mismos datos que el objeto
     * UserDto proporcionado
     */
    public static final Users toUser(UserDto userDto, Users trainer) throws IllegalArgumentException {

        String role = userDto.getRole();

        if (Users.RoleType.TRAINER.toString().equals(role)) {
            return new Users(userDto.getEmail(), userDto.getPassword(),
                    userDto.getFullName(), userDto.getPhone(), userDto.getIcon(),
                    userDto.getSocialLinks());
        } else if (Users.RoleType.CLIENT.toString().equals(role)) {

            LocalDate birthdate = null;

            if (userDto.getBirthdate() != null && !userDto.getBirthdate().isEmpty()) {
                birthdate = LocalDate.parse(userDto.getBirthdate());
            }

            return new Users(userDto.getEmail(), userDto.getPassword(),
                    userDto.getFullName(), userDto.getPhone(), userDto.getIcon(),
                    birthdate, userDto.getInjuries(), userDto.getGoals(),
                    userDto.getHeight(), trainer);
        } else {
            throw new IllegalArgumentException("Invalid role: " + userDto.getRole());
        }

    }

    /**
     * Convierte un objeto Users a un objeto AuthenticatedUserDto.
     *
     * @param serviceToken el token de servicio del usuario autenticado
     * @param user el objeto Users a convertir
     * @return un nuevo objeto AuthenticatedUserDto que contiene los mismos
     * datos que el objeto Users proporcionado, junto con el token de servicio
     */
    public static final AuthenticatedUserDto toAuthenticatedUserDto(String serviceToken, Users user) {

        return new AuthenticatedUserDto(serviceToken, toUserDto(user));

    }

}
