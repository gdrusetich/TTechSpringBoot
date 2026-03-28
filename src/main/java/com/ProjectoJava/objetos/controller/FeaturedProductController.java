package com.ProjectoJava.objetos.controller;

import com.ProjectoJava.objetos.DTO.request.FeaturedProductRequestDTO;
import com.ProjectoJava.objetos.DTO.response.FeaturedProductResponseDTO;
import com.ProjectoJava.objetos.service.FeaturedProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/featured")
public class FeaturedProductController {

    @Autowired
    private FeaturedProductService featuredService;

    @GetMapping
    public ResponseEntity<List<FeaturedProductResponseDTO>> isFeatured() {
        return ResponseEntity.ok(featuredService.getAllFeaturedDTOs());
    }

    @PostMapping("/add")
    public ResponseEntity<String> addFeatured(@RequestBody FeaturedProductRequestDTO request) {
        featuredService.addFeatured(request.productId());
        return ResponseEntity.ok("Producto destacado con éxito.");
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<String> removeFeatured(@PathVariable Long productId) {
        featuredService.removeFeatured(productId);
        return ResponseEntity.ok("Producto quitado de destacados.");
    }

    @PatchMapping("/reorder")
    public ResponseEntity<String> reorder(@RequestParam Long productId, @RequestParam int newPosition) {
        featuredService.updatePosition(productId, newPosition);
        return ResponseEntity.ok("Posición actualizada.");
    }
}