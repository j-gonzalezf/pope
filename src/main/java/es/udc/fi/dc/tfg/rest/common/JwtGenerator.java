package es.udc.fi.dc.tfg.rest.common;

/**
 * Interfaz JwtGenerator.
 */
public interface JwtGenerator {

    /**
     * Genera un token JWT a partir de la información proporcionada.
     *
     * @param info Información del usuario para generar el token
     * @return Un string con el token JWT generado
     */
    String generate(JwtInfo info);

    /**
     * Obtiene la información del usuario a partir de un token JWT.
     *
     * @param token El token JWT del que se extraerá la información
     * @return La información del usuario extraída del token
     */
    JwtInfo getInfo(String token);

}
