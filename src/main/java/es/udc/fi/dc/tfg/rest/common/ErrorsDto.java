package es.udc.fi.dc.tfg.rest.common;

import java.util.List;

/**
 * Clase ErrorsDto.
 */
public class ErrorsDto {

    /**
     * El global error.
     */
    private String globalError;

    /**
     * Los field errors.
     */
    private List<FieldErrorDto> fieldErrors;

    /**
     * Crea una nueva instancia de ErrorsDto con un error global.
     *
     * @param globalError El mensaje de error global
     */
    public ErrorsDto(String globalError) {
        setGlobalError(globalError);
    }

    /**
     * Crea una nueva instancia de ErrorsDto con una lista de errores de campo.
     *
     * @param fieldErrors La lista de errores de campo
     */
    public ErrorsDto(List<FieldErrorDto> fieldErrors) {
        setFieldErrors(fieldErrors);
    }

    /**
     * Gets the global error.
     *
     * @return the global error
     */
    public String getGlobalError() {
        return globalError;
    }

    /**
     * Sets the global error.
     *
     * @param globalError the new global error
     */
    public void setGlobalError(String globalError) {
        this.globalError = globalError;
    }

    /**
     * Gets the field errors.
     *
     * @return the field errors
     */
    public List<FieldErrorDto> getFieldErrors() {
        return fieldErrors;
    }

    /**
     * Sets the field errors.
     *
     * @param fieldErrors the new field errors
     */
    public void setFieldErrors(List<FieldErrorDto> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

}
