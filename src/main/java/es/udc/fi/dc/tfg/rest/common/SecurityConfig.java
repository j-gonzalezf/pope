package es.udc.fi.dc.tfg.rest.common;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Clase SecurityConfig.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    /**
     * Configura la cadena de filtros de seguridad de la aplicación.
     *
     * @param http Objeto HttpSecurity para configurar
     * @return La cadena de filtros de seguridad configurada
     * @throws Exception Si ocurre un error durante la configuración
     */
    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // @formatter:off
        http.cors(cors -> cors.disable()).csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(antMatcher("/*")).permitAll()
                .requestMatchers(antMatcher("/static/**")).permitAll()
                .requestMatchers(antMatcher("/assets/**")).permitAll()
                .requestMatchers(antMatcher("/api/hello")).permitAll()
                .requestMatchers(antMatcher("/api/users/signUp")).permitAll()
                .requestMatchers(antMatcher("/api/users/login")).permitAll()
                .requestMatchers(antMatcher("/api/users/loginFromServiceToken")).permitAll()
                .requestMatchers(antMatcher("/api/users/addClient")).hasRole("TRAINER")
                .requestMatchers(antMatcher("/api/users/{id}/clients")).hasRole("TRAINER")
                .requestMatchers(antMatcher("/api/users/{id}")).hasRole("TRAINER")
                .requestMatchers(antMatcher("/api/users/{id}/changePassword")).hasRole("TRAINER")
                .requestMatchers(antMatcher("/api/users/{id}/delete")).hasRole("TRAINER")
                .requestMatchers(antMatcher("/api/templates/cycle/create")).hasRole("TRAINER")
                .requestMatchers(antMatcher("/api/templates/{id}")).hasRole("TRAINER")
                .requestMatchers(antMatcher("/api/exercises/exercise/add")).hasRole("TRAINER")
                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        // @formatter:on

        return http.build();
    }

    /**
     * Authentication manager.
     *
     * @param authenticationConfiguration Configuración de autenticación
     * @return El gestor de autenticación
     * @throws Exception Si ocurre un error al obtener el gestor de
     * autenticación
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Cors configuration source.
     *
     * @return the cors configuration source
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/**", config);

        return source;

    }

}
