package com.ProjectoJava.objetos.controller;

import com.ProjectoJava.objetos.DTO.response.CategoryResponseDTO;
import com.ProjectoJava.objetos.entity.Category;
import com.ProjectoJava.objetos.repository.CategoryRepository;
import com.ProjectoJava.objetos.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{id}/ancestros")
    public List<CategoryResponseDTO> getAncestros(@PathVariable Long id) {
        return service.obtenerAncestrosDTO(id);
    }

    @PostMapping("/add")
    public ResponseEntity<Category> agregar(@RequestBody Category nueva) {
        return ResponseEntity.ok(repository.save(nueva));
    }
    @PutMapping("/update/{id}")
    public Category update(@PathVariable Long id, @RequestBody Category category) {
        category.setId(id);
        return repository.save(category);
    }

}