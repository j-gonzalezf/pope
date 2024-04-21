package es.udc.fi.dc.tfg.rest.dtos;

import es.udc.fi.dc.tfg.model.entities.Users;

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

        return new UserDto(user.getId(), user.getEmail(), user.getFullName(),
                user.getPhone(), user.getIcon(), user.getUserRole().toString(),
                user.getSocialLinks(), user.getBirthdate(), user.getInjuries(),
                user.getGoals(), user.getHeight());

    }

    /**
     * Convierte un objeto UserDto a un objeto Users.
     *
     * @param userDto el objeto UserDto a convertir
     * @return un nuevo objeto Users que contiene los mismos datos que el objeto
     * UserDto proporcionado
     */
    public static final Users toUser(UserDto userDto) throws IllegalArgumentException {

        if (userDto.getRole().equals(Users.RoleType.TRAINER.toString())) {

            return new Users(userDto.getEmail(), userDto.getPassword(),
                    userDto.getFullName(), userDto.getPhone(), userDto.getIcon(),
                    userDto.getSocialLinks());

        } else if (userDto.getRole().equals(Users.RoleType.CLIENT.toString())) {

            return new Users(userDto.getEmail(), userDto.getPassword(),
                    userDto.getFullName(), userDto.getPhone(), userDto.getIcon(),
                    userDto.getBirthdate(), userDto.getInjuries(),
                    userDto.getGoals(), userDto.getHeight());

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
