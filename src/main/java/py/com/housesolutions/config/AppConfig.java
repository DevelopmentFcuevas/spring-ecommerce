package py.com.housesolutions.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class AppConfig {
    /*
     * Spring Boot puede automáticamente detectar estos archivos(messages_es.properties) si están en el directorio
     * de recursos (src/main/resources). Sin embargo, se está personalizando la configuración
     * en esta clase de configuración.
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages_es");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

}
