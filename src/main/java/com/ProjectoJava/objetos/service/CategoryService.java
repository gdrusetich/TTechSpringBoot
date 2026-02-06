package com.ProjectoJava.objetos.service;

import com.ProjectoJava.objetos.repository.CategoryRepository;
import com.ProjectoJava.objetos.DTO.response.CategoryResponseDTO;
import com.ProjectoJava.objetos.entity.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;


@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category buscarPorId(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La categoría con ID " + id + " no existe."));
    }

    public List<Category> obtenerCategoriasPadre() {
        return categoryRepository.findByParentIsNull();
    }

    public List<Category> obtenerSubcategorias(Long padreId) {
        return categoryRepository.findByParent_Id(padreId);
    }

    public List<Category> obtenerAncestros(Long categoryId) {
        List<Category> ancestros = new ArrayList<>();
        
        Category actual = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        
        ancestros.add(actual);
        
        while (actual.getParent() != null) {
            actual = actual.getParent();
            ancestros.add(actual);
        }
        return ancestros;
    }
    
    @GetMapping("/all")
    public List<Category> listarTodas() {
        return categoryRepository.findAllByOrderByNameAsc();
    }

    public List<CategoryResponseDTO> obtenerAncestrosDTO(Long categoryId) {
        List<Category> entidades = obtenerAncestros(categoryId); // Usás la lógica que ya tenías
        return entidades.stream()
                        .map(CategoryResponseDTO::new)
                        .collect(Collectors.toList());
    }

}