package com.ProjectoJava.objetos.repository;

import com.ProjectoJava.objetos.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Aquí no hace falta escribir código, Spring ya sabe hacer todo el CRUD
}