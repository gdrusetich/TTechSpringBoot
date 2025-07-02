package com.ProjectoJava.objetos.controller;
import java.util.ArrayList;

import exceptions.ProductExistsException;


import com.ProjectoJava.objetos.entity.Product;
import com.ProjectoJava.objetos.service.ProductService;
import exceptions.ProductNotExistsException;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController         //Anotación que explicita a Springboot que la clase es un Controller
@RequestMapping("/products")
public class ProductController {
    private ProductService service;

    @Autowired
    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/list")                //Para obtener datos, utilizo Get.
    public ArrayList<Product> listarProductos(){
        return service.listarProductos();
    }

    @PostMapping("/")
    public String crearProducto(@RequestBody Product nuevoProducto){
        return "creando producto... \n"+
                "Nombre: " + nuevoProducto.getNombre() + "\n" +
                "Precio: " + nuevoProducto.getPrecio() +"\n"+
                "Stock: " + nuevoProducto.getStock() + "\n";
    }

    @PostMapping("/nuevo-producto")
    public ResponseEntity<String> agregarProducto(@RequestBody Product nuevoProducto) throws ProductExistsException{
        try {
            service.agregarProducto(nuevoProducto);
            return ResponseEntity.ok("Producto Agregado");
        } catch (ProductExistsException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/find-id/{productId}")
    public ResponseEntity<String> buscarProductoPorId(@PathVariable int id) throws ProductNotExistsException {      //La variable lo busca en la ruta
        try {
            return ResponseEntity.ok(service.buscarProductoPorId(id).mostrarInfo());
        } catch (ProductNotExistsException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable int id) {
        try {
            service.eliminarProductoPorId(id);
            return ResponseEntity.ok("Producto eliminado");
        } catch (ProductNotExistsException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
