package es.udc.fi.dc.tfg.rest.controllers;

import static es.udc.fi.dc.tfg.rest.dtos.UserConversor.toAuthenticatedUserDto;
import static es.udc.fi.dc.tfg.rest.dtos.UserConversor.toUser;
import static es.udc.fi.dc.tfg.rest.dtos.UserConversor.toUserDto;
import static es.udc.fi.dc.tfg.rest.dtos.UserConversor.toUsersDto;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.udc.fi.dc.tfg.model.common.exceptions.DuplicateInstanceException;
import es.udc.fi.dc.tfg.model.common.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.tfg.model.entities.Users;
import es.udc.fi.dc.tfg.model.services.exceptions.IncorrectLoginException;
import es.udc.fi.dc.tfg.model.services.exceptions.IncorrectPasswordException;
import es.udc.fi.dc.tfg.model.services.exceptions.InvalidRoleException;
import es.udc.fi.dc.tfg.model.services.exceptions.PermissionException;
import es.udc.fi.dc.tfg.model.services.UserService;
import es.udc.fi.dc.tfg.rest.common.ErrorsDto;
import es.udc.fi.dc.tfg.rest.common.JwtGenerator;
import es.udc.fi.dc.tfg.rest.common.JwtInfo;
import es.udc.fi.dc.tfg.rest.dtos.AuthenticatedUserDto;
import es.udc.fi.dc.tfg.rest.dtos.ChangePasswordParamsDto;
import es.udc.fi.dc.tfg.rest.dtos.LoginParamsDto;
import es.udc.fi.dc.tfg.rest.dtos.UserDto;

/**
 * Clase UserController.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    /**
     * La constante INCORRECT_LOGIN_EXCEPTION_CODE.
     */
    private static final String INCORRECT_LOGIN_EXCEPTION_CODE = "project.exceptions.IncorrectLoginException";

    /**
     * La constante INCORRECT_PASSWORD_EXCEPTION_CODE.
     */
    private static final String INCORRECT_PASS_EXCEPTION_CODE = "project.exceptions.IncorrectPasswordException";

    /**
     * El message source.
     */
    @Autowired
    private MessageSource messageSource;

    /**
     * El jwt generator.
     */
    @Autowired
    private JwtGenerator jwtGenerator;

    /**
     * El user service.
     */
    @Autowired
    private UserService userService;

    /**
     * Maneja la excepción de inicio de sesión incorrecto.
     *
     * @param exception la excepción de inicio de sesión incorrecto
     * @param locale la ubicación geográfica del usuario
     * @return un objeto ErrorsDto con el mensaje de error
     */
    @ExceptionHandler(IncorrectLoginException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorsDto handleIncorrectLoginException(IncorrectLoginException exception, Locale locale) {

        String errorMessage = messageSource.getMessage(INCORRECT_LOGIN_EXCEPTION_CODE, null,
                INCORRECT_LOGIN_EXCEPTION_CODE, locale);

        return new ErrorsDto(errorMessage);

    }

    /**
     * Maneja la excepción de contraseña incorrecta.
     *
     * @param exception la excepción de contraseña incorrecta
     * @param locale la ubicación geográfica del usuario
     * @return un objeto ErrorsDto con el mensaje de error
     */
    @ExceptionHandler(IncorrectPasswordException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorsDto handleIncorrectPasswordException(IncorrectPasswordException exception, Locale locale) {

        String errorMessage = messageSource.getMessage(INCORRECT_PASS_EXCEPTION_CODE, null,
                INCORRECT_PASS_EXCEPTION_CODE, locale);

        return new ErrorsDto(errorMessage);

    }

    // Método común para registrar un usuario
    private Users createUser(UserDto userDto, Users trainer)
            throws DuplicateInstanceException {

        Users user = toUser(userDto, trainer);

        userService.signUp(user);

        return user;

    }

    /**
     * Registrar un nuevo entrenador.
     *
     * @param userDto el DTO del usuario
     * @return una ResponseEntity que contiene un AuthenticatedUserDto
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email.
     */
    @PostMapping("/signUp")
    public ResponseEntity<AuthenticatedUserDto> signUp(@Validated({UserDto.AllValidations.class})
            @RequestBody UserDto userDto) throws DuplicateInstanceException {

        Users user = createUser(userDto, null);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(location).body(toAuthenticatedUserDto(generateServiceToken(user), user));

    }

    /**
     * Registrar un nuevo cliente.
     *
     * @param userId el ID del usuario que realiza la petición
     * @param userDto el DTO del usuario
     * @return una ResponseEntity que contiene un UserDto
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email.
     * @throws InstanceNotFoundException si no se encuentra un usuario con el
     * trainerId proporcionado.
     * @throws PermissionException si el ID del usuario que realiza la petición
     * no coincide con el ID del entrenador del cliente que se va a crear.
     * @throws InvalidRoleException si el cliente que se va a crear no tiene rol
     */
    @PostMapping("/addClient")
    public ResponseEntity<UserDto> addClient(@RequestAttribute Long userId,
            @Validated({UserDto.AllValidations.class}) @RequestBody UserDto userDto)
            throws DuplicateInstanceException, InstanceNotFoundException,
            PermissionException, InvalidRoleException {

        Users trainer = userService.loginFromId(userDto.getTrainerId());

        userService.validateUser(userId, trainer.getId());

        Users user = createUser(userDto, trainer);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(location).body(toUserDto(user));

    }

    /**
     * Login.
     *
     * @param params the params
     * @return the authenticated user dto
     * @throws IncorrectLoginException the incorrect login exception
     */
    @PostMapping("/login")
    public AuthenticatedUserDto login(@Validated @RequestBody LoginParamsDto params)
            throws IncorrectLoginException {

        Users user = userService.login(params.getEmail(), params.getPassword());

        return toAuthenticatedUserDto(generateServiceToken(user), user);

    }

    /**
     * Login from service token.
     *
     * @param userId el ID del usuario
     * @param serviceToken el service token
     * @return el authenticated user dto
     * @throws InstanceNotFoundException si no se encuentra un usuario con el ID
     * proporcionado.
     */
    @PostMapping("/loginFromServiceToken")
    public AuthenticatedUserDto loginFromServiceToken(@RequestAttribute Long userId,
            @RequestAttribute String serviceToken) throws InstanceNotFoundException {

        Users user = userService.loginFromId(userId);

        return toAuthenticatedUserDto(serviceToken, user);

    }

    /**
     * Devuelve la lista de clientes de un entrenador.
     *
     * @param userId el ID del usuario que realiza la petición
     * @param id el ID del entrenador
     * @return una lista de clientes
     * @throws InstanceNotFoundException si no se encuentra ningún entrenador
     * con ese ID
     * @throws PermissionException si el ID del usuario que realiza la petición
     * no coincide con el ID del entrenador
     * @throws InvalidRoleException si el entrenador no tiene rol
     */
    @GetMapping("/{id}/clients")
    public List<UserDto> getClients(@RequestAttribute Long userId, @PathVariable("id") Long id)
            throws InstanceNotFoundException, PermissionException, InvalidRoleException {

        userService.validateUser(userId, id);

        List<Users> clients = userService.getClients(id);

        return toUsersDto(clients);

    }

    /**
     * Devuelve el cliente de un entrenador a partir de su ID.
     *
     * @param userId el ID del usuario que realiza la petición
     * @param clientId el ID del cliente
     * @return un cliente
     * @throws InstanceNotFoundException si no se encuentra ningún cliente
     * @throws PermissionException si el ID del usuario que realiza la petición
     * no coincide con el trainer ID del cliente que se solicita
     * @throws InvalidRoleException si el cliente no tiene rol
     */
    @GetMapping("/client/{clientId}")
    public UserDto getClientInfo(@RequestAttribute Long userId,
            @PathVariable("clientId") Long clientId)
            throws InstanceNotFoundException, PermissionException, InvalidRoleException {

        userService.validateUser(userId, clientId);

        Users client = userService.loginFromId(clientId);

        return toUserDto(client);

    }

    /**
     * Actualizar perfil.
     *
     * @param userId el ID del usuario que realiza la petición
     * @param id el ID del usuario al que se le va a actualizar el perfil
     * @param userDto el user dto
     * @return el user dto
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email
     * @throws InstanceNotFoundException si no se encuentra un usuario con el ID
     * proporcionado
     * @throws PermissionException si el ID del usuario que realiza la petición
     * no coincide con el ID del usuario al que se le va a actualizar el perfil
     * @throws InvalidRoleException si el usuario no tiene rol
     */
    @PutMapping("/{id}")
    public UserDto updateProfile(@RequestAttribute Long userId, @PathVariable("id") Long id,
            @Validated({UserDto.UpdateValidations.class}) @RequestBody UserDto userDto)
            throws DuplicateInstanceException, InstanceNotFoundException,
            PermissionException, InvalidRoleException {

        userService.validateUser(userId, id);

        Users user = userService.loginFromId(id);
        String role = user.getUserRole().toString();

        switch (role) {

            case "TRAINER" -> {
                return toUserDto(userService.updateProfile(id, userDto.getEmail(),
                        userDto.getFullName(), userDto.getPhone(),
                        userDto.getIcon(), userDto.getSocialLinks()));
            }

            case "CLIENT" -> {
                LocalDate birthdate = null;

                // Comprobamos si la fecha es nula para que no falle .parse
                if (userDto.getBirthdate() != null && !userDto.getBirthdate().isEmpty()) {
                    birthdate = LocalDate.parse(userDto.getBirthdate());
                }

                return toUserDto(userService.updateClient(id, userDto.getEmail(),
                        userDto.getFullName(), userDto.getPhone(), userDto.getIcon(),
                        birthdate, userDto.getInjuries(), userDto.getGoals(), userDto.getHeight()));
            }

            default ->
                throw new InvalidRoleException();

        }

    }

    /**
     * Cambia la contraseña de un usuario.
     *
     * @param userId el ID del usuario que realiza la petición
     * @param id el ID del usuario al que se le va a cambiar la contraseña
     * @param params los parámetros de cambio de contraseña, que incluyen la
     * contraseña antigua y la nueva
     * @throws InstanceNotFoundException si no se encuentra un usuario con el ID
     * proporcionado
     * @throws PermissionException si el ID del usuario que realiza la petición
     * no coincide con el ID del usuario al que se le va a cambiar la contraseña
     * @throws InvalidRoleException si el usuario no tiene rol
     * @throws IncorrectPasswordException si la contraseña antigua proporcionada
     * no coincide con la contraseña actual del usuario
     */
    @PostMapping("/{id}/changePassword")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@RequestAttribute Long userId, @PathVariable Long id,
            @Validated @RequestBody ChangePasswordParamsDto params)
            throws InstanceNotFoundException, PermissionException,
            InvalidRoleException, IncorrectPasswordException {

        userService.validateUser(userId, id);

        userService.changePassword(id, params.getOldPassword(), params.getNewPassword());

    }

    /**
     * Elimina un usuario.
     *
     * @param userId el ID del usuario que realiza la petición
     * @param id el ID del usuario al que se le va a eliminar la cuenta
     * @return el ID del usuario que ha sido eliminado
     * @throws InstanceNotFoundException si no se encuentra un usuario con el ID
     * proporcionado
     * @throws PermissionException si el ID del usuario que realiza la petición
     * no coincide con el ID del usuario al que se va a eliminar
     * @throws InvalidRoleException si el usuario no tiene rol
     */
    @DeleteMapping("/{id}/delete")
    public Long deleteUser(@RequestAttribute Long userId, @PathVariable("id") Long id)
            throws InstanceNotFoundException, PermissionException, InvalidRoleException {

        userService.validateUser(userId, id);

        return userService.deleteUser(id);

    }

    /**
     * Genera un service token para el usuario.
     *
     * @param user el usuario para el que se va a generar el token
     * @return el service token generado
     */
    private String generateServiceToken(Users user) {

        JwtInfo jwtInfo = new JwtInfo(user.getId(), user.getEmail(), user.getUserRole().toString());

        return jwtGenerator.generate(jwtInfo);

    }

}
