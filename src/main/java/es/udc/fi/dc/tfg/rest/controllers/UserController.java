package es.udc.fi.dc.tfg.rest.controllers;

import static es.udc.fi.dc.tfg.rest.dtos.UserConversor.toAuthenticatedUserDto;
import static es.udc.fi.dc.tfg.rest.dtos.UserConversor.toUser;
import static es.udc.fi.dc.tfg.rest.dtos.UserConversor.toUserDto;
import static es.udc.fi.dc.tfg.rest.dtos.UserConversor.toUsersDto;
import static es.udc.fi.dc.tfg.rest.dtos.UserConversor.toWeightsDto;
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
import es.udc.fi.dc.tfg.model.entities.Weights;
import es.udc.fi.dc.tfg.model.services.exceptions.IncorrectLoginException;
import es.udc.fi.dc.tfg.model.services.exceptions.IncorrectPasswordException;
import es.udc.fi.dc.tfg.model.services.exceptions.InvalidRoleException;
import es.udc.fi.dc.tfg.model.services.exceptions.PermissionException;
import es.udc.fi.dc.tfg.model.services.UserExtraService;
import es.udc.fi.dc.tfg.model.services.UserService;
import es.udc.fi.dc.tfg.rest.common.ErrorsDto;
import es.udc.fi.dc.tfg.rest.common.JwtGenerator;
import es.udc.fi.dc.tfg.rest.common.JwtInfo;
import es.udc.fi.dc.tfg.rest.dtos.AuthenticatedUserDto;
import es.udc.fi.dc.tfg.rest.dtos.ChangePasswordParamsDto;
import es.udc.fi.dc.tfg.rest.dtos.LoginParamsDto;
import es.udc.fi.dc.tfg.rest.dtos.UserDto;
import es.udc.fi.dc.tfg.rest.dtos.WeightDto;
import java.io.IOException;
import java.time.LocalDateTime;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

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
     * El user extra service.
     */
    @Autowired
    private UserExtraService userExtraService;

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
    private Users createUser(UserDto userDto, Users trainer, MultipartFile file)
            throws DuplicateInstanceException, IOException {

        Users user = toUser(userDto, trainer);

        userService.signUp(user, file);

        return user;

    }

    /**
     * Registrar un nuevo entrenador.
     *
     * @param userDto el DTO del usuario
     * @param file el icono del usuario
     * @return una ResponseEntity que contiene un AuthenticatedUserDto
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email.
     * @throws IOException si hay algún error a la hora de guardar la imagen
     */
    @PostMapping("/signUp")
    public ResponseEntity<AuthenticatedUserDto> signUp(@RequestPart("user")
            @Validated({UserDto.AllValidations.class}) UserDto userDto,
            @RequestPart(value = "file", required = false) MultipartFile file)
            throws DuplicateInstanceException, IOException {

        Users user = createUser(userDto, null, file);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(location).body(toAuthenticatedUserDto(generateServiceToken(user), user));

    }

    /**
     * Registrar un nuevo cliente.
     *
     * @param userId el ID del usuario que realiza la petición
     * @param userDto el DTO del usuario
     * @param file el icono del usuario
     * @return una ResponseEntity que contiene un UserDto
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email.
     * @throws InstanceNotFoundException si no se encuentra un usuario con el
     * trainerId proporcionado.
     * @throws PermissionException si el ID del usuario que realiza la petición
     * no coincide con el ID del entrenador del cliente que se va a crear.
     * @throws InvalidRoleException si el cliente que se va a crear no tiene rol
     * @throws IOException si hay algún error a la hora de guardar la imagen
     */
    @PostMapping("/addClient")
    public ResponseEntity<UserDto> addClient(@RequestAttribute Long userId,
            @RequestPart("user") @Validated({UserDto.AllValidations.class}) UserDto userDto,
            @RequestPart(value = "file", required = false) MultipartFile file)
            throws DuplicateInstanceException, InstanceNotFoundException,
            PermissionException, InvalidRoleException, IOException {

        Users trainer = userService.loginFromId(userDto.getTrainerId());

        userService.validateUser(userId, trainer.getId());

        Users user = createUser(userDto, trainer, file);

        Weights weight = null;
        if (userDto.getWeight() != null) {
            weight = new Weights(userDto.getWeight(), LocalDateTime.now(), user);
            userExtraService.weightRegister(weight);
        }

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(location).body(toUserDto(user, weight));

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
        Weights weight = userExtraService.getLastWeight(clientId);

        return toUserDto(client, weight);

    }
    
    /**
     * Devuelve el entrenador de un cliente a partir de su ID.
     *
     * @param userId el ID del usuario que realiza la petición
     * @param trainerId el ID del entrenador
     * @return un cliente
     * @throws InstanceNotFoundException si no se encuentra ningún entrenador
     * @throws PermissionException si el ID del usuario que realiza la petición
     * no coincide con el trainer ID del cliente que se solicita
     * @throws InvalidRoleException si el cliente no tiene rol
     */
    @GetMapping("/trainer/{trainerId}")
    public UserDto getTrainerInfo(@RequestAttribute Long userId,
            @PathVariable("trainerId") Long trainerId)
            throws InstanceNotFoundException, PermissionException, InvalidRoleException {

        Users user = userService.loginFromId(userId);
        
        userService.validateUser(user.getTrainer().getId(), trainerId);

        Users trainer = userService.loginFromId(trainerId);

        return toUserDto(trainer, null);

    }

    /**
     * Actualizar perfil.
     *
     * @param userId el ID del usuario que realiza la petición
     * @param id el ID del usuario al que se le va a actualizar el perfil
     * @param userDto el user dto
     * @param file el icono del usuario
     * @return el user dto
     * @throws DuplicateInstanceException si ya existe un usuario con el mismo
     * email
     * @throws InstanceNotFoundException si no se encuentra un usuario con el ID
     * proporcionado
     * @throws PermissionException si el ID del usuario que realiza la petición
     * no coincide con el ID del usuario al que se le va a actualizar el perfil
     * @throws InvalidRoleException si el usuario no tiene rol
     * @throws IOException si hay algún error a la hora de guardar la imagen
     */
    @PutMapping("/{id}")
    public UserDto updateProfile(@RequestAttribute Long userId, @PathVariable("id") Long id,
            @RequestPart("user") @Validated({UserDto.UpdateValidations.class}) UserDto userDto,
            @RequestPart(value = "file", required = false) MultipartFile file)
            throws DuplicateInstanceException, InstanceNotFoundException,
            PermissionException, InvalidRoleException, IOException {

        userService.validateUser(userId, id);

        Users user = userService.loginFromId(id);
        String role = user.getUserRole().toString();

        switch (role) {

            case "TRAINER" -> {
                return toUserDto(userService.updateProfile(id, userDto.getEmail(),
                        userDto.getFullName(), userDto.getPhone(),
                        file, userDto.getSocialLinks()), null);
            }

            case "CLIENT" -> {
                LocalDate birthdate = null;

                // Comprobamos si la fecha es nula para que no falle .parse
                if (userDto.getBirthdate() != null && !userDto.getBirthdate().isEmpty()) {
                    birthdate = LocalDate.parse(userDto.getBirthdate());
                }

                Weights lastWeight = userExtraService.getLastWeight(id);
                Weights newWeight = null;
                if (userDto.getWeight() != null && (lastWeight == null
                        || !lastWeight.getWeight().equals(userDto.getWeight()))) {
                    newWeight = new Weights(userDto.getWeight(), LocalDateTime.now(), user);
                    userExtraService.weightRegister(newWeight);

                }

                Users updatedClient = userService.updateClient(id, userDto.getEmail(),
                        userDto.getFullName(), userDto.getPhone(), file,
                        birthdate, userDto.getInjuries(), userDto.getGoals(),
                        userDto.getHeight());

                return toUserDto(updatedClient, newWeight != null ? newWeight : lastWeight);

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
     * Devuelve la lista de registros de pesos de un cliente.
     *
     * @param userId el ID del usuario que realiza la petición
     * @param clientId el ID del cliente
     * @return una lista de registros
     * @throws InstanceNotFoundException si no se encuentra un cliente con el ID
     * proporcionado
     * @throws PermissionException si el ID del usuario que realiza la petición
     * no coincide con el ID del entrenador de los ciclos a obtener
     * @throws InvalidRoleException si el usuario que se va validar no tiene rol
     */
    @GetMapping("/weights/fromClient/{clientId}")
    public List<WeightDto> getWeights(@RequestAttribute Long userId,
            @PathVariable("clientId") Long clientId) throws InstanceNotFoundException,
            PermissionException, InvalidRoleException {

        userService.validateUser(userId, clientId);

        List<Weights> weights = userExtraService.getWeights(clientId);

        return toWeightsDto(weights);

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
