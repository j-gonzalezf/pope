package es.udc.fi.dc.tfg.rest.common;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import es.udc.fi.dc.tfg.model.common.exceptions.DuplicateInstanceException;
import es.udc.fi.dc.tfg.model.common.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.tfg.model.services.exceptions.PermissionException;
import es.udc.fi.dc.tfg.model.services.exceptions.InvalidRoleException;

/**
 * Clase CommonControllerAdvice.
 */
@ControllerAdvice
public class CommonControllerAdvice {

    /**
     * La Constante INSTANCE_NOT_FOUND_EXCEPTION_CODE.
     */
    private static final String INSTANCE_NOT_FOUND_EXCEPTION_CODE = "project.exceptions.InstanceNotFoundException";

    /**
     * La Constante DUPLICATE_INSTANCE_EXCEPTION_CODE.
     */
    private static final String DUPLICATE_INSTANCE_EXCEPTION_CODE = "project.exceptions.DuplicateInstanceException";

    /**
     * La Constante PERMISSION_EXCEPTION_CODE.
     */
    private static final String PERMISSION_EXCEPTION_CODE = "project.exceptions.PermissionException";

    /**
     * La Constante INVALID_ROLE_EXCEPTION_CODE.
     */
    private static final String INVALID_ROLE_EXCEPTION_CODE = "project.exceptions.InvalidRoleException";

    /**
     * El message source.
     */
    @Autowired
    private MessageSource messageSource;

    /**
     * Maneja las excepciones de argumentos de método no válidos.
     *
     * @param exception La excepción que se va a manejar
     * @return Un objeto ErrorsDto con los errores de campo
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorsDto handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

        List<FieldErrorDto> fieldErrors = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> new FieldErrorDto(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        return new ErrorsDto(fieldErrors);

    }

    /**
     * Maneja las excepciones de instancia no encontrada.
     *
     * @param exception La excepción que se va a manejar
     * @param locale El locale para la internacionalización
     * @return Un objeto ErrorsDto con el error global
     */
    @ExceptionHandler(InstanceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorsDto handleInstanceNotFoundException(InstanceNotFoundException exception, Locale locale) {

        String nameMessage = messageSource.getMessage(exception.getName(), null,
                exception.getName(), locale);

        String errorMessage = messageSource.getMessage(INSTANCE_NOT_FOUND_EXCEPTION_CODE,
                new Object[]{nameMessage, exception.getKey().toString()},
                INSTANCE_NOT_FOUND_EXCEPTION_CODE, locale);

        return new ErrorsDto(errorMessage);

    }

    /**
     * Maneja las excepciones de instancia duplicada.
     *
     * @param exception La excepción que se va a manejar
     * @param locale El locale para la internacionalización
     * @return Un objeto ErrorsDto con el error global
     */
    @ExceptionHandler(DuplicateInstanceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorsDto handleDuplicateInstanceException(DuplicateInstanceException exception, Locale locale) {

        String nameMessage = messageSource.getMessage(exception.getName(), null,
                exception.getName(), locale);

        String errorMessage = messageSource.getMessage(DUPLICATE_INSTANCE_EXCEPTION_CODE,
                new Object[]{nameMessage, exception.getKey().toString()},
                DUPLICATE_INSTANCE_EXCEPTION_CODE, locale);

        return new ErrorsDto(errorMessage);

    }

    /**
     * Maneja las excepciones de permisos.
     *
     * @param exception La excepción que se va a manejar
     * @param locale El locale para la internacionalización
     * @return Un objeto ErrorsDto con el error global
     */
    @ExceptionHandler(PermissionException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorsDto handlePermissionException(PermissionException exception, Locale locale) {

        String errorMessage = messageSource.getMessage(PERMISSION_EXCEPTION_CODE,
                null, PERMISSION_EXCEPTION_CODE, locale);

        return new ErrorsDto(errorMessage);

    }

    /**
     * Maneja las excepciones de rol invalido.
     *
     * @param exception La excepción que se va a manejar
     * @param locale El locale para la internacionalización
     * @return Un objeto ErrorsDto con el error global
     */
    @ExceptionHandler(InvalidRoleException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorsDto handleInvalidRoleException(InvalidRoleException exception, Locale locale) {

        String errorMessage = messageSource.getMessage(INVALID_ROLE_EXCEPTION_CODE,
                null, INVALID_ROLE_EXCEPTION_CODE, locale);

        return new ErrorsDto(errorMessage);

    }

}
