package com.ProjectoJava.objetos.controller;

import com.ProjectoJava.objetos.DTO.request.FeaturedProductRequestDTO;
import com.ProjectoJava.objetos.DTO.response.FeaturedProductResponseDTO;
import com.ProjectoJava.objetos.service.FeaturedProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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

    @PostMapping("/set-timer")
    public ResponseEntity<String> setExpiration(@RequestBody Map<String, String> payload) {
        String fechaIso = payload.get("fecha");
        try {
            LocalDateTime fecha = LocalDateTime.parse(fechaIso);
            featuredService.setExpirationDate(fecha);
            return ResponseEntity.ok("Reloj configurado con éxito.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Formato de fecha inválido.");
        }
    }

    @GetMapping("/timer")
    public ResponseEntity<LocalDateTime> getExpiration() {
        return ResponseEntity.ok(featuredService.getExpirationDate());
    }
    
    @DeleteMapping("/clear-timer")
    public ResponseEntity<String> clearTimer() {
        featuredService.setExpirationDate(null);
        return ResponseEntity.ok("Temporizador cancelado.");
    }
}