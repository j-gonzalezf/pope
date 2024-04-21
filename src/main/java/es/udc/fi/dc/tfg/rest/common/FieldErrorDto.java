package es.udc.fi.dc.tfg.rest.common;

/**
 * Clase FieldErrorDto.
 */
public class FieldErrorDto {

    /**
     * El nombre del campo.
     */
    private String fieldName;

    /**
     * El mensaje de error.
     */
    private String message;

    /**
     * Crea una nueva instancia de FieldErrorDto.
     *
     * @param fieldName El nombre del campo que causó el error
     * @param message El mensaje de error asociado con el campo
     */
    public FieldErrorDto(String fieldName, String message) {

        setFieldName(fieldName);
        setMessage(message);

    }

    /**
     * Gets the field name.
     *
     * @return the field name
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Sets the field name.
     *
     * @param fieldName the new field name
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * Gets the message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message.
     *
     * @param message the new message
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
