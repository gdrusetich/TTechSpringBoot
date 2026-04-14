package com.ProjectoJava.objetos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@SpringBootApplication
public class ClaseSpringbootApplication {
	public static void main(String[] args) {
		SpringApplication.run(ClaseSpringbootApplication.class, args);
		
		}

		@Bean
	public Cloudinary cloudinaryConfig() {
		return new Cloudinary(ObjectUtils.asMap(
			"cloud_name", System.getenv("CLOUDINARY_CLOUD_NAME"),
			"api_key", System.getenv("CLOUDINARY_API_KEY"),
			"api_secret", System.getenv("CLOUDINARY_API_SECRET")));
	}
}