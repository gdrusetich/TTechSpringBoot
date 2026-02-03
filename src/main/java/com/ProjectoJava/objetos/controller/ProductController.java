package com.ProjectoJava.objetos.controller;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile; // Para recibir la imagen

import com.ProjectoJava.objetos.DTO.request.ProductRequestDTO;
import com.ProjectoJava.objetos.DTO.response.ProductResponseDTO;
import com.ProjectoJava.objetos.entity.Product;
import com.ProjectoJava.objetos.repository.ProductRepository;

import exceptions.ProductExistsException;
//import org.springframework.stereotype.Controller;
import com.ProjectoJava.objetos.service.ProductService;
import exceptions.ProductNotExistsException;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@RestController         //Anotación que explicita a Springboot que la clase es un Controller
@CrossOrigin(origins = "*")
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService service;
    @Autowired
    private ProductRepository repository;

    @Autowired
    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/list")
    public List<ProductResponseDTO> listarProductos(){
        return service.listarProductos();
    }

    @GetMapping("/categoria/{categoryId}")
    public List<ProductResponseDTO> obtenerProductosPorCategoria(@PathVariable Long categoryId) {
        System.out.println("Solicitando productos para categoría ID: " + categoryId);
        return service.filtrarPorCategoria(categoryId);
    }

    @PostMapping("/nuevo-producto")
    public ResponseEntity<?> agregarProducto(
    @RequestParam("title") String title,
    @RequestParam("price") Double price,
    @RequestParam("stock") Integer stock,
    @RequestParam("description") String description,
    @RequestParam("category") Set<Long> categoriesId,
    @RequestParam(value = "images", required = false) List<MultipartFile> images) throws ProductExistsException, IOException{
        try {
            List<String> nombresArchivos = new ArrayList<>();

            if (images != null && !images.isEmpty()) {
                        for (MultipartFile image : images) {
                            if (image != null && !image.isEmpty()) {
                                String nombreFinal = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
                                Path ruta = Paths.get("uploads").resolve(nombreFinal).toAbsolutePath();
      
                                if (!Files.exists(ruta.getParent())) Files.createDirectories(ruta.getParent());
                                
                                Files.copy(image.getInputStream(), ruta, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                                nombresArchivos.add(nombreFinal); // Guardamos el nombre en una lista
                            }
                        }
                    } 
            ProductRequestDTO productoCreado = new ProductRequestDTO(title, price, stock, description, categoriesId, nombresArchivos);
            return ResponseEntity.ok(service.agregarProducto(productoCreado));
        } catch (ProductExistsException e) {
            return ResponseEntity.badRequest().body("Error: "+e.getMessage());
        }
    }

    @GetMapping("/find-id/{id}")
    public ResponseEntity<?> buscarProductoPorId(@PathVariable Long id) throws ProductNotExistsException {      //La variable lo busca en la ruta
        try {
            ProductResponseDTO productoBuscado = service.buscarProductoPorId(id);
            return ResponseEntity.ok(productoBuscado);
        } catch (ProductNotExistsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: "+e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) throws ProductNotExistsException {
        try {
            service.eliminarProductoPorId(id);
            return ResponseEntity.ok("Producto "+id+" eliminado");
        } catch (ProductNotExistsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: "+e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> actualizar(
        @PathVariable Long id, 
        @RequestParam("title") String title,
        @RequestParam("price") Double price,
        @RequestParam("stock") Integer stock,
        @RequestParam("description") String description,
        @RequestParam("category") Set<Long> categoriesId,
        @RequestParam(value = "images", required = false) List<MultipartFile> images // Imagen opcional
    ) {
        try {
            List<String> nombreArchivosNuevos = new ArrayList<>();
            // 1. Si el usuario subió una imagen nueva, la procesamos
            if (images != null && !images.isEmpty()) {
                for(MultipartFile img: images){
                    if(!img.isEmpty()){
                        String nombreFinal = UUID.randomUUID().toString() + "_" + img.getOriginalFilename();
                        Path ruta = Paths.get("uploads").resolve(nombreFinal).toAbsolutePath();
                        String extension = "";
                        if(nombreFinal != null && nombreFinal.contains(".")){
                            extension = nombreFinal.substring(nombreFinal.lastIndexOf("."));
                        } else {
                            extension = ".jpg";
                        }
                        if(!Files.exists(ruta.getParent()))
                            Files.createDirectories(ruta.getParent());

                        Files.copy(img.getInputStream(), ruta, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                        nombreArchivosNuevos.add(nombreFinal);
                    }
                }
            }
            
            ProductRequestDTO dto = new ProductRequestDTO(title, price, stock, description, categoriesId, nombreArchivosNuevos);

            ProductResponseDTO actualizado = service.actualizarProducto(id, dto);
            return ResponseEntity.ok(actualizado);            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar: " + e.getMessage());
        }
    }
    
    @GetMapping("/filter-price-{precioMaximo}")
    public List<ProductResponseDTO> listarMasBaratos(@PathVariable double precioMaximo) throws ProductNotExistsException{
        return service.filtrarPorPrecio(precioMaximo);
    }

    @GetMapping("/home")
    public String mostrarCatalogo(HttpSession session) {
        return "home"; 
    }

}
