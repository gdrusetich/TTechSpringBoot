package com.ProjectoJava.objetos.repository;
import com.ProjectoJava.objetos.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
    boolean existsByNombreIgnoreCase(String nombre);
}
