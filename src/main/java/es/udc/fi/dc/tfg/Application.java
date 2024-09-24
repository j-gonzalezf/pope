package es.udc.fi.dc.tfg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Clase Application.
 */
@SpringBootApplication
public class Application {

    /**
     * El método main que inicia la aplicación.
     *
     * @param args Argumentos de la línea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Password encoder.
     *
     * @return Un nuevo codificador de contraseñas BCrypt
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura el MessageSource para la internacionalización de mensajes del
     * lado del servidor (backend).
     *
     * @return El MessageSource configurado con la codificación por defecto en
     * UTF-8.
     */
    @Bean
    public MessageSource messageSource() {

        ReloadableResourceBundleMessageSource bean = new ReloadableResourceBundleMessageSource();

        bean.setBasename("classpath:messages/messages");
        bean.setDefaultEncoding("UTF-8");

        return bean;
    }

    /**
     * Validator.
     *
     * @return Un nuevo validador local con el message source establecido.
     */
    @Bean
    public LocalValidatorFactoryBean validator() {

        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();

        bean.setValidationMessageSource(messageSource());

        return bean;
    }

}
