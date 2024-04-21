package es.udc.fi.dc.tfg.rest.common;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * Clase JwtGeneratorImpl.
 */
@Component
public class JwtGeneratorImpl implements JwtGenerator {

    /** La sign key. */
    @Value("${project.jwt.signKey}")
    private String signKey;

    /** La expiration minutes. */
    @Value("${project.jwt.expirationMinutes}")
    private long expirationMinutes;

    /**
     * Genera un token JWT a partir de la información proporcionada.
     *
     * @param info Información del usuario para generar el token
     * @return Un string con el token JWT generado
     */
    @Override
    public String generate(JwtInfo info) {

        Claims claims = Jwts.claims();

        claims.setSubject(info.getEmail())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMinutes * 60 * 1000));
        claims.put("userId", info.getUserId());
        claims.put("role", info.getRole());
        
        return Jwts.builder().setClaims(claims).signWith(Keys.hmacShaKeyFor(signKey.getBytes()), SignatureAlgorithm.HS512).compact();

    }

    /**
     * Obtiene la información del usuario a partir de un token JWT.
     *
     * @param token El token JWT del que se extraerá la información
     * @return La información del usuario extraída del token
     */
    @Override
    public JwtInfo getInfo(String token) {
        
        Claims claims = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(signKey.getBytes())).build().parseClaimsJws(token).getBody();

        return new JwtInfo(((Integer) claims.get("userId")).longValue(), claims.getSubject(),
                (String) claims.get("role"));

    }

}
