package com.ProjectoJava.objetos.controller;

import com.ProjectoJava.objetos.DTO.response.CategoryResponseDTO;
import com.ProjectoJava.objetos.entity.Category;
import com.ProjectoJava.objetos.repository.CategoryRepository;
import com.ProjectoJava.objetos.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "*") // Para que el frontend pueda entrar
public class CategoryController {

    @Autowired
    private CategoryRepository repository;
    
    @Autowired
    private CategoryService service;

    @GetMapping("/list")
    public List<Category> listar() {
        return repository.findAll();
    }

    @GetMapping("/all")
    public List<CategoryResponseDTO> getAllCategories() {
        return repository.findAll()
                .stream()
                .map(cat -> new CategoryResponseDTO(cat))
                .collect(Collectors.toList());
    }
    @GetMapping("/{id}/ancestros")
    public List<CategoryResponseDTO> getAncestros(@PathVariable Long id) {
        return service.obtenerAncestrosDTO(id);
    }

    @GetMapping("/{id}/hijos")
    public List<CategoryResponseDTO> getHijos(@PathVariable Long id) {
        List<Category> hijas = service.obtenerSubcategorias(id);
        return hijas.stream()
                    .map(CategoryResponseDTO::new)
                    .collect(Collectors.toList());
    }

    @PostMapping("/add")
    public ResponseEntity<?> agregar(@RequestBody Category nueva) {
        try {
            // Si el JS mandó un padre con ID, lo vinculamos correctamente
            if (nueva.getParent() != null && nueva.getParent().getId() != null) {
                Category padre = repository.findById(nueva.getParent().getId())
                    .orElseThrow(() -> new RuntimeException("Padre no encontrado"));
                nueva.setParent(padre);
            } else {
                nueva.setParent(null);
            }

            repository.save(nueva);
            return ResponseEntity.ok().body("{\"status\": \"success\"}");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/update/{id}")
    public Category update(@PathVariable Long id, @RequestBody Category category) {
        category.setId(id);
        return repository.save(category);
    }

}