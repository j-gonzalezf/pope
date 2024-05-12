package es.udc.fi.dc.tfg.rest.controllers;

import static es.udc.fi.dc.tfg.rest.dtos.UserConversor.toAuthenticatedUserDto;
import static es.udc.fi.dc.tfg.rest.dtos.UserConversor.toUser;
import static es.udc.fi.dc.tfg.rest.dtos.UserConversor.toUserDto;
import static es.udc.fi.dc.tfg.rest.dtos.UserConversor.toUsersDto;
import java.net.URI;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
import es.udc.fi.dc.tfg.model.services.exceptions.PermissionException;
import es.udc.fi.dc.tfg.model.services.UserService;
import es.udc.fi.dc.tfg.rest.common.ErrorsDto;
import es.udc.fi.dc.tfg.rest.common.JwtGenerator;
import es.udc.fi.dc.tfg.rest.common.JwtInfo;
import es.udc.fi.dc.tfg.rest.dtos.AuthenticatedUserDto;
import es.udc.fi.dc.tfg.rest.dtos.ChangePasswordParamsDto;
import es.udc.fi.dc.tfg.rest.dtos.LoginParamsDto;
import es.udc.fi.dc.tfg.rest.dtos.UserDto;
import java.time.LocalDate;
import org.springframework.web.bind.annotation.GetMapping;

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
     * La constante ILLEGAL_ARGUMENT_EXCEPTION
     */
    private static final String ILLEGAL_ARGUMENT_EXCEPTION = "project.exceptions.IllegalArgumentException";

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

    /**
     * Maneja la excepción de rol inválido.
     *
     * @param exception la excepción de rol inválido
     * @param locale la ubicación geográfica del usuario
     * @return un objeto ErrorsDto con el mensaje de error
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorsDto handleIllegalArgumentException(IllegalArgumentException exception, Locale locale) {

        String errorMessage = messageSource.getMessage(ILLEGAL_ARGUMENT_EXCEPTION, null,
                ILLEGAL_ARGUMENT_EXCEPTION, locale);

        return new ErrorsDto(errorMessage);

    }

    // Método común para registrar un usuario
    private Users createUser(UserDto userDto)
            throws DuplicateInstanceException, IllegalArgumentException, InstanceNotFoundException {

        Users trainer = null;

        if (userDto.getTrainerId() != null) {
            trainer = userService.loginFromId(userDto.getTrainerId());
        }

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
     * @throws InstanceNotFoundException si no se encuentra un usuario con el ID
     * proporcionado.
     */
    @PostMapping("/signUp")
    public ResponseEntity<AuthenticatedUserDto> signUp(
            @Validated({UserDto.AllValidations.class}) @RequestBody UserDto userDto)
            throws DuplicateInstanceException, IllegalArgumentException, InstanceNotFoundException {

        Users user = createUser(userDto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(location).body(toAuthenticatedUserDto(generateServiceToken(user), user));

    }

    /**
     * Registrar un nuevo cliente.
     *
     * @param userDto el DTO del usuario
     * @return una ResponseEntity que contiene un AuthenticatedUserDto
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email.
     * @throws InstanceNotFoundException si no se encuentra un usuario con el ID
     * proporcionado.
     */
    @PostMapping("/addClient")
    public ResponseEntity<UserDto> addClient(
            @Validated({UserDto.AllValidations.class}) @RequestBody UserDto userDto)
            throws DuplicateInstanceException, IllegalArgumentException, InstanceNotFoundException {

        Users user = createUser(userDto);

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
    public AuthenticatedUserDto login(@Validated @RequestBody LoginParamsDto params) throws IncorrectLoginException {

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
     * Actualizar perfil.
     *
     * @param userId el ID del usuario que realiza la petición
     * @param id el ID del usuario al que se le va a actualizar el perfil
     * @param userDto el user dto
     * @return el user dto
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email.
     * @throws InstanceNotFoundException si no se encuentra un usuario con el ID
     * proporcionado.
     * @throws PermissionException si el ID del usuario que realiza la petición
     * no coincide con el ID del usuario al que se le va a actualizar el perfil
     */
    @PutMapping("/{id}")
    public UserDto updateProfile(@RequestAttribute Long userId, @PathVariable("id") Long id,
            @Validated({UserDto.UpdateValidations.class}) @RequestBody UserDto userDto)
            throws DuplicateInstanceException, InstanceNotFoundException, PermissionException {

        if (!id.equals(userId)) {
            throw new PermissionException();
        }

        Users user = userService.loginFromId(userId);
        String role = user.getUserRole().toString();

        if (Users.RoleType.TRAINER.toString().equals(role)) {
            return toUserDto(userService.updateProfile(id, userDto.getEmail(),
                    userDto.getFullName(), userDto.getPhone(),
                    userDto.getIcon(), userDto.getSocialLinks()));
        } else if (Users.RoleType.CLIENT.toString().equals(role)) {
            return toUserDto(userService.updateClient(id, userDto.getEmail(),
                    userDto.getFullName(), userDto.getPhone(), userDto.getIcon(),
                    LocalDate.parse(userDto.getBirthdate()), userDto.getInjuries(),
                    userDto.getGoals(), userDto.getHeight()));
        } else {
            throw new IllegalArgumentException("Invalid role: " + userDto.getRole());
        }

    }

    /**
     * Cambia la contraseña de un usuario.
     *
     * @param userId el ID del usuario que realiza la petición
     * @param id el ID del usuario al que se le va a cambiar la contraseña
     * @param params los parámetros de cambio de contraseña, que incluyen la
     * contraseña antigua y la nueva
     * @throws PermissionException si el ID del usuario que realiza la petición
     * no coincide con el ID del usuario al que se le va a cambiar la contraseña
     * @throws InstanceNotFoundException si no se encuentra un usuario con el ID
     * proporcionado
     * @throws IncorrectPasswordException si la contraseña antigua proporcionada
     * no coincide con la contraseña actual del usuario
     */
    @PostMapping("/{id}/changePassword")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@RequestAttribute Long userId, @PathVariable Long id,
            @Validated @RequestBody ChangePasswordParamsDto params)
            throws PermissionException, InstanceNotFoundException, IncorrectPasswordException {

        if (!id.equals(userId)) {
            throw new PermissionException();
        }

        userService.changePassword(id, params.getOldPassword(), params.getNewPassword());

    }

    /**
     * Devuelve la lista de clientes de un entrenador.
     *
     * @param id el ID del entrenador
     * @return una lista de clientes
     * @throws InstanceNotFoundException si no se encuentra ningún cliente
     */
    @GetMapping("/{id}/clients")
    public List<UserDto> getClients(@PathVariable Long id) throws InstanceNotFoundException {
        List<Users> clients = userService.getClients(id);
        return toUsersDto(clients);
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
