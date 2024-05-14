package es.udc.fi.dc.tfg.rest.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Clase AuthenticatedUserDto.
 */
public class AuthenticatedUserDto {

    /**
     * El service token.
     */
    private String serviceToken;

    /**
     * El user dto.
     */
    private UserDto userDto;

    /**
     * Instancia un nuevo authenticated user dto.
     */
    public AuthenticatedUserDto() {
    }

    /**
     * Instancia un nuevo authenticated user dto.
     *
     * @param serviceToken el service token
     * @param userDto el user dto
     */
    public AuthenticatedUserDto(String serviceToken, UserDto userDto) {

        setServiceToken(serviceToken);
        setUserDto(userDto);

    }

    /**
     * Gets the service token.
     *
     * @return the service token
     */
    public String getServiceToken() {
        return serviceToken;
    }

    /**
     * Sets the service token.
     *
     * @param serviceToken the new service token
     */
    public void setServiceToken(String serviceToken) {
        this.serviceToken = serviceToken;
    }

    /**
     * Gets the user dto.
     *
     * @return the user dto
     */
    @JsonProperty("user")
    public UserDto getUserDto() {
        return userDto;
    }

    /**
     * Sets the user dto.
     *
     * @param userDto the new user dto
     */
    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

}
