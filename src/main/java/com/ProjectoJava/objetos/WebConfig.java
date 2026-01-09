package com.ProjectoJava.objetos;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry; // Opcional, por si tienes problemas de CORS

@Configuration
public class WebConfig implements WebMvcConfigurer {

@Override
public void addResourceHandlers(ResourceHandlerRegistry registry) {
    // 1. Obtenemos la ruta absoluta y la limpiamos para Windows
    java.io.File uploadDir = new java.io.File("uploads");
    String rootPath = uploadDir.getAbsolutePath();
    
    // 2. Forzamos el formato de URI correcto para Windows
    String resourcePath = "file:///" + rootPath.replace("\\", "/") + "/";

    registry.addResourceHandler("/uploads/**")
            .addResourceLocations(resourcePath)
            .setCachePeriod(0);

    // 3. ESTO ES VITAL: Mira lo que sale en tu consola de VS Code al arrancar
    System.out.println("=============================================");
    System.out.println("RUTA FÍSICA: " + rootPath);
    System.out.println("RUTA PARA SPRING: " + resourcePath);
    System.out.println("¿LA CARPETA EXISTE?: " + uploadDir.exists());
    System.out.println("=============================================");
}
    // Ya que estamos aquí, esto ayuda si el Frontend y Backend tienen problemas de permisos
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}