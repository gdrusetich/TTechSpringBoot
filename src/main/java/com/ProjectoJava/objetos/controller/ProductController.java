package com.ProjectoJava.objetos.controller;

import com.ProjectoJava.objetos.service.FeaturedProductService;
import com.ProjectoJava.objetos.service.ImageService;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.nio.file.Files;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile; // Para recibir la imagen
import org.springframework.ui.Model;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.Optional;

import com.ProjectoJava.objetos.DTO.request.ProductRequestDTO;
import com.ProjectoJava.objetos.DTO.response.ProductResponseDTO;
import com.ProjectoJava.objetos.entity.Product;
import com.ProjectoJava.objetos.entity.Category;
import com.ProjectoJava.objetos.entity.Role;
import com.ProjectoJava.objetos.entity.Image;
import com.ProjectoJava.objetos.repository.ProductRepository;
import com.ProjectoJava.objetos.repository.CategoryRepository;
import com.ProjectoJava.objetos.repository.ImageRepository;

import com.ProjectoJava.objetos.service.ProductService;
import exceptions.ProductNotExistsException;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController         //Anotación que explicita a Springboot que la clase es un Controller
@CrossOrigin(origins = "*")
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService service;

    @Autowired
    private FeaturedProductService featuredService;

    @Autowired
    private ImageService imageService;


    @Autowired
    private ProductRepository repository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/list")
    public List<ProductResponseDTO> listarParaHome() {
        return service.listarProductosVisibles();
    }

    @GetMapping("/all")
    @ResponseBody 
    public List<ProductResponseDTO> listarParaAdmin() {
        return service.listarTodoCompleto(); 
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
        @RequestParam(value = "featured", defaultValue = "false") Boolean featured, 
        @RequestParam(value = "mainImageId", required = false) Long mainImageId, 
        @RequestParam(value = "images", required = false) List<MultipartFile> images) {

        try {
            List<String> nombresArchivos = new ArrayList<>();
            
            Path directorioUploads = Paths.get("uploads");
            if (!Files.exists(directorioUploads)) {
                Files.createDirectories(directorioUploads);
            }

            if (images != null) {
                for (MultipartFile image : images) {
                    if (!image.isEmpty()) {
                        String nombreFinal = image.getOriginalFilename();
                        Path rutaCompleta = directorioUploads.resolve(nombreFinal);
                        
                        Files.copy(image.getInputStream(), rutaCompleta, StandardCopyOption.REPLACE_EXISTING);
                        nombresArchivos.add(nombreFinal);
                    }
                }
            }

            ProductRequestDTO dto = new ProductRequestDTO(
                title, 
                price, 
                java.time.LocalDate.now(), 
                false,    // oculto
                featured, // <--- Pasamos el nuevo valor
                stock, 
                description, 
                categoriesId, 
                mainImageId, 
                nombresArchivos
            );
            
            return ResponseEntity.ok(service.agregarProducto(dto));

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al guardar el producto o las imágenes: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/toggle-visible")
    @ResponseBody
    public ResponseEntity<?> toggleVisible(@PathVariable Long id) {
        Product p = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        p.setOculto(!p.isOculto()); // Invertimos el Booleano
        repository.save(p);
        
        return ResponseEntity.ok().build();
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

    @PostMapping("/create-masivo")
    public ResponseEntity<?> crearMasivo(@RequestBody ProductRequestDTO dto) {
        try {
            Product p = new Product();
            p.setTitle(dto.getTitle());
            p.setPrice(dto.getPrice());
            p.setStock(dto.getStock());
            p.setDescription(dto.getDescription());
            
            // Usamos tus nombres de métodos reales:
            p.setFechaUltimoPrecio(java.time.LocalDate.now());
            p.setOculto(false); 
            p.setFeatured(false);

            if (dto.getCategoryNames() != null && !dto.getCategoryNames().isEmpty()) {
                
                java.util.Set<Category> categorias = dto.getCategoryNames().stream()
                    .map(nombre -> categoryRepository.findByName(nombre.trim())) // <-- ACÁ usamos el REPO
                    .filter(java.util.Optional::isPresent)
                    .map(java.util.Optional::get)
                    .collect(java.util.stream.Collectors.toSet());
                    
                p.setCategories(categorias);
            }
            
            // 3. Guardamos el producto
            repository.save(p);
            
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/update-title/{id}")
    public ResponseEntity<?> updateProductTitle(@PathVariable Long id, @RequestParam String title) {
        Product producto = repository.findById(id).orElse(null);
        if (producto == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
        producto.setTitle(title);
        repository.save(producto);        
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/update-price/{id}")
    public ResponseEntity<?> updateProductPrice(@PathVariable Long id, 
                                                @RequestParam Double price, // Cambiamos a RequestParam
                                                HttpSession session) {

        Object roleAttr = session.getAttribute("userRole");
        if (roleAttr == null || !roleAttr.toString().equals(Role.ADMIN.name())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado");
        }

        Product p = repository.findById(id).orElse(null);
        if (p == null) return ResponseEntity.notFound().build();

        p.setPrice(price);
        p.setFechaUltimoPrecio(LocalDate.now()); // ¡Excelente que mantengas la fecha!
        repository.save(p);
        
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
            List<Category> listaCategorias = categoryRepository.findAllById(categoryIds);
            Set<Category> setCategorias = new HashSet<>(listaCategorias);
            producto.setCategories(setCategorias);
            repository.save(producto);
            
            return ResponseEntity.ok("Categorías actualizadas correctamente");
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/main-image/{imageId}")
    public ResponseEntity<?> setMainImage(@PathVariable Long id, @PathVariable Long imageId) {
        try {
            service.establecerImagenPrincipal(id, imageId);
            return ResponseEntity.ok("Imagen principal actualizada");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<?> eliminarImagen(@PathVariable Long imageId) {
        imageService.deleteImage(imageId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{productId}/add-images")
    public ResponseEntity<?> agregarImagenesAProducto(
            @PathVariable Long productId,
            @RequestParam("images") MultipartFile[] files) {
        
        try {
            imageService.addImagesToProduct(productId, files);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al subir: " + e.getMessage());
        }
    }

    @DeleteMapping("/{productId}/images/all")
    public ResponseEntity<?> eliminarTodasLasImagenes(@PathVariable Long productId) {
        imageService.deleteAllImagesByProductId(productId); 
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{productId}/categorias-jerarquia")
    public ResponseEntity<Map<String, List<Category>>> obtenerJerarquia(@PathVariable Long productId) {
        Map<String, List<Category>> mapa = service.obtenerMapaCategoriasPorProducto(productId);
        return ResponseEntity.ok(mapa);
    }

    @PostMapping("/images/uploads")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file, 
                                        @RequestParam("productId") Long productId) {
        try {
            // 1. Definir el directorio de destino
            Path directorioPath = Paths.get("uploads");
            
            // 2. CREAR EL DIRECTORIO SI NO EXISTE (Esto salva el deploy)
            if (!Files.exists(directorioPath)) {
                Files.createDirectories(directorioPath);
            }

            String fileName = file.getOriginalFilename();
            // 3. Resolve es más seguro que concatenar con "/"
            Path targetPath = directorioPath.resolve(fileName);
            
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            Product producto = repository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            Image nuevaImagen = new Image();
            // Guardamos solo el nombre del archivo (o la ruta que uses para mostrarla)
            nuevaImagen.setUrl(fileName); 
            nuevaImagen.setProduct(producto);
            imageRepository.save(nuevaImagen);

            return ResponseEntity.ok().build();
        } catch (IOException e) {
            // Imprimí el error en la consola de Render para ver qué pasa si vuelve a fallar
            e.printStackTrace(); 
            return ResponseEntity.status(500).body("Error al guardar archivo: " + e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(404).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/actualizar-rapido/{id}")
    public ResponseEntity<?> actualizarProductoRapido(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        try {
            return repository.findById(id).map(p -> {
                // Extraemos los valores del mapa con cuidado
                if (payload.containsKey("title")) {
                    p.setTitle(payload.get("title").toString());
                }
                if (payload.containsKey("price")) {
                    p.setPrice(Double.parseDouble(payload.get("price").toString()));
                }
                if (payload.containsKey("stock")) {
                    p.setStock(Integer.parseInt(payload.get("stock").toString()));
                }
                
                p.setFechaUltimoPrecio(java.time.LocalDate.now());
                repository.save(p);
                return ResponseEntity.ok().build();
            }).orElse(ResponseEntity.notFound().build());
            
        } catch (Exception e) {
            e.printStackTrace(); 
            return ResponseEntity.status(500).body("Error interno: " + e.getMessage());
        }
    }

    @PatchMapping("/{id}/destacar")
    public ResponseEntity<?> gestionarFeatured(@PathVariable Long id, @RequestBody Map<String, Boolean> payload) {
        return repository.findById(id).map(p -> {
            Boolean nuevoEstado = payload.get("featured");
            if (nuevoEstado != null) {
                p.setFeatured(nuevoEstado);
                repository.save(p);

                if (nuevoEstado) {
                    featuredService.addFeatured(p.getId());
                } else {
                    featuredService.removeFeatured(p.getId());
                }

                return ResponseEntity.ok().build();
            }
            return ResponseEntity.badRequest().body("Falta el campo featured");
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/admin/revision-precios")
    public String filtrarPreciosViejos(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFiltro, 
            Model model) {
        
        List<Product> productosViejos;
        
        if (fechaFiltro != null) {
            productosViejos = repository.findByFechaUltimoPrecioBefore(fechaFiltro);
        } else {
            productosViejos = new ArrayList<>();
        }
        
        model.addAttribute("productos", productosViejos);
        model.addAttribute("fechaSeleccionada", fechaFiltro);
        
        return "admin/control-precios"; 
    }

}
