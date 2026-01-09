package com.ProjectoJava.objetos.controller;
import java.util.List;
import java.util.UUID;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile; // Para recibir la imagen


import com.ProjectoJava.objetos.DTO.request.ProductRequestDTO;
import com.ProjectoJava.objetos.DTO.response.ProductResponseDTO;
import exceptions.ProductExistsException;

import com.ProjectoJava.objetos.service.ProductService;
import exceptions.ProductNotExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController         //Anotación que explicita a Springboot que la clase es un Controller
@CrossOrigin(origins = "*")
/*@CrossOrigin(origins = "${frontend.url}")*/
@RequestMapping("/products")
public class ProductController {
    private ProductService service;

    @Autowired
    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/list")                //Para obtener datos, utilizo Get.
    public List<ProductResponseDTO> listarProductos(){
        return service.listarProductos();
    }

    @PostMapping("/nuevo-producto")
    public ResponseEntity<?> agregarProducto(
    @RequestParam("title") String title,
    @RequestParam("price") Double price,
    @RequestParam("stock") Integer stock,
    @RequestParam("category") Long categoryId,
    @RequestParam(value = "image", required = false) MultipartFile image) throws ProductExistsException{
        try {
            String nombreFinal = null;
            if(image!= null && !image.isEmpty()) {
                nombreFinal = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
                try {
                    if (image != null && !image.isEmpty()) {
                        nombreFinal = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
                        Path ruta = Paths.get("uploads").resolve(nombreFinal).toAbsolutePath();                        
                        Files.copy(image.getInputStream(), ruta, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    return ResponseEntity.internalServerError().body("Error al guardar la imagen: " + e.getMessage());
                }
            } 
            ProductRequestDTO productoCreado = new ProductRequestDTO(title, price, stock, categoryId, nombreFinal);
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
        @RequestParam("category") Long categoryId,
        @RequestParam(value = "image", required = false) MultipartFile image // Imagen opcional
    ) {
        try {
            String nombreImagen = null;            
            // 1. Si el usuario subió una imagen nueva, la procesamos
            if (image != null && !image.isEmpty()) {
                String originalName = image.getOriginalFilename();
                String extension = "";
                if(originalName != null && originalName.contains(".")){
                    extension = originalName.substring(originalName.lastIndexOf("."));
                } else {
                    extension = ".jpg";
                }
                nombreImagen = UUID.randomUUID().toString() + extension; 
                Path ruta = Paths.get("uploads").resolve(nombreImagen).toAbsolutePath();
                if(!Files.exists(ruta.getParent())){
                    Files.createDirectories(ruta.getParent());
                }

                Files.copy(image.getInputStream(), ruta, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Archivo guardado físicamente como: " + nombreImagen);
            }

            // 2. Creamos el DTO con los datos recibidos
            ProductRequestDTO dto = new ProductRequestDTO(title, price, stock, categoryId, nombreImagen);

            // 3. El service hace el resto
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

}
