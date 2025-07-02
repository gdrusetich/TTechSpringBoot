package com.ProjectoJava.objetos;

import com.ProjectoJava.objetos.repository.ProductRepository;
import com.ProjectoJava.objetos.repository.OrderRepository;
import com.ProjectoJava.objetos.entity.Product;
import exceptions.NoStockException;
import exceptions.ProductNotExistsException;
import exceptions.ProductExistsException;

import com.ProjectoJava.objetos.service.Menu;

import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClaseSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClaseSpringbootApplication.class, args);

	}

}
