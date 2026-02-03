package com.ProjectoJava.objetos.controller;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
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
import com.ProjectoJava.objetos.entity.Category;
import com.ProjectoJava.objetos.repository.ProductRepository;
import com.ProjectoJava.objetos.repository.CategoryRepository;

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
    private CategoryRepository categoryRepository;

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
    
    @GetMapping("/filter-price-{precioMaximo}")
    public List<ProductResponseDTO> listarMasBaratos(@PathVariable double precioMaximo) throws ProductNotExistsException{
        return service.filtrarPorPrecio(precioMaximo);
    }

    @GetMapping("/home")
    public String mostrarCatalogo(HttpSession session) {
        return "home"; 
    }

    @PutMapping("/update-title/{id}")
    public ResponseEntity<?> updateProductTitle(@PathVariable Long id, @RequestParam String title) {
        // Buscamos el producto directamente
        Product producto = repository.findById(id).orElse(null);
        if (producto == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
        producto.setTitle(title);
        repository.save(producto); // Un método simple que haga repository.save
        
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update-price/{id}")
    public ResponseEntity<?> updateProductPrice(@PathVariable Long id, @RequestParam Double price) throws ProductNotExistsException {
        Product producto = repository.findById(id).orElse(null);
        if (producto == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
        producto.setPrice(price);
        repository.save(producto); // Un método simple que haga repository.save
        
        return ResponseEntity.ok().build();

    }

    @PutMapping("/update-stock/{id}")
    public ResponseEntity<?> updateProductStock(@PathVariable Long id, @RequestParam Integer stock) throws ProductNotExistsException {
        Product producto = repository.findById(id).orElse(null);
        if (producto == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
        producto.setStock(stock);
        repository.save(producto); // Un método simple que haga repository.save
        
        return ResponseEntity.ok().build();

    }

    @PutMapping("/update-description/{id}")
    public ResponseEntity<?> updateProductDescription(@PathVariable Long id, @RequestParam String description) throws ProductNotExistsException {
        Product producto = repository.findById(id).orElse(null);
        if (producto == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
        producto.setDescription(description);
        repository.save(producto); // Un método simple que haga repository.save
        
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/categories")
    public ResponseEntity<?> updateProductCategories(@PathVariable Long id, @RequestBody List<Long> categoryIds) {
        return repository.findById(id).map(producto -> {
            // Buscamos las entidades completas basadas en los IDs que mandó el frontend
            List<Category> listaCategorias = categoryRepository.findAllById(categoryIds);
            Set<Category> setCategorias = new HashSet<>(listaCategorias);            
            // Seteamos la nueva lista (JPA se encarga de limpiar la tabla intermedia y reinsertar)
            producto.setCategories(setCategorias);
            repository.save(producto);
            
            return ResponseEntity.ok("Categorías actualizadas correctamente");
        }).orElse(ResponseEntity.notFound().build());
    }

}
