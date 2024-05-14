package es.udc.fi.dc.tfg.rest.common;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Clase JwtFilter.
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    /**
     * El jwt generator.
     */
    @Autowired
    private JwtGenerator jwtGenerator;

    /**
     * Realiza el filtrado interno de las solicitudes HTTP. Extrae el token de
     * autorización del encabezado de la solicitud, valida el token y configura
     * el contexto de seguridad.
     *
     * @param request la solicitud HTTP entrante
     * @param response la respuesta HTTP saliente
     * @param filterChain la cadena de filtros de la solicitud
     * @throws ServletException si ocurre un error al procesar la solicitud
     * @throws IOException si ocurre un error de entrada/salida
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeaderValue = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeaderValue == null || !authHeaderValue.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {

            String serviceToken = authHeaderValue.replace("Bearer ", "");
            JwtInfo jwtInfo = jwtGenerator.getInfo(serviceToken);

            request.setAttribute("serviceToken", serviceToken);
            request.setAttribute("userId", jwtInfo.getUserId());

            configureSecurityContext(jwtInfo.getEmail(), jwtInfo.getRole());

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);

    }

    /**
     * Configura el security context para el usuario autenticado.
     *
     * @param email el email del usuario autenticado
     * @param role el rol del usuario autenticado
     */
    private void configureSecurityContext(String email, String role) {

        Set<GrantedAuthority> authorities = new HashSet<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));

        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(email, null, authorities));

    }

}
