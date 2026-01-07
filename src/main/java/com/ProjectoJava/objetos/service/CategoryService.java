package com.ProjectoJava.objetos.service;

import com.ProjectoJava.objetos.repository.CategoryRepository;
import com.ProjectoJava.objetos.entity.Category;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category buscarPorId(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La categoría con ID " + id + " no existe."));
    }
    
    // Aquí también podrías tener el método para listar que usa el Controller
    public List<Category> listarTodas() {
        return categoryRepository.findAll();
    }
}