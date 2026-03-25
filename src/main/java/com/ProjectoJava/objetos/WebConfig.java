package com.ProjectoJava.objetos;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry; // Opcional, por si tienes problemas de CORS

@Configuration
public class WebConfig implements WebMvcConfigurer {

@Override
public void addResourceHandlers(ResourceHandlerRegistry registry) {
    // Para las fotos dinámicas (las que sube el Turco)
    registry.addResourceHandler("/products/images/**") // La URL que usás en el HTML
            .addResourceLocations("file:images/")      // La carpeta real en el servidor
            .setCachePeriod(0);

    // Para las fotos fijas (logos, estilos, etc)
    registry.addResourceHandler("/static/**")
            .addResourceLocations("classpath:/static/");
}

    @Override
    public void addCorsMappings(@SuppressWarnings("null") CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}