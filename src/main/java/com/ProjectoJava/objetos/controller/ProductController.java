package com.ProjectoJava.objetos.controller;
import java.util.ArrayList;
import java.util.List;

import exceptions.ProductExistsException;


import com.ProjectoJava.objetos.entity.Product;
import com.ProjectoJava.objetos.service.ProductService;
import exceptions.ProductNotExistsException;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


//TODO Pasar todo a DTO.

@RestController         //Anotación que explicita a Springboot que la clase es un Controller
@RequestMapping("/products")
public class ProductController {
    private ProductService service;

    @Autowired
    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/list")                //Para obtener datos, utilizo Get.
    public List<Product> listarProductos(){
        return service.listarProductos();
    }
/*
    @PostMapping("/")
    public String crearProducto(@RequestBody Product nuevoProducto){
        return "creando producto... \n"+
                "Nombre: " + nuevoProducto.getNombre() + "\n" +
                "Precio: " + nuevoProducto.getPrecio() +"\n"+
                "Stock: " + nuevoProducto.getStock() + "\n";
    }
*/  //CORROBORAR
    @PostMapping("/nuevo-producto")
    public ResponseEntity<Product> agregarProducto(@RequestBody Product nuevoProducto) throws ProductExistsException{
        try {
            service.agregarProducto(nuevoProducto);
            return ResponseEntity.status(HttpStatus.CREATED).body(this.service.agregarProducto(nuevoProducto));
        } catch (ProductExistsException e) {
            return ResponseEntity.badRequest().body(nuevoProducto);
        }
    }

    @GetMapping("/find-id/{productId}")
    public ResponseEntity<Product> buscarProductoPorId(@PathVariable Long id) throws ProductNotExistsException {      //La variable lo busca en la ruta
        try {
            return ResponseEntity.ok(service.buscarProductoPorId(id));
        } catch (ProductNotExistsException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        try {
            service.eliminarProductoPorId(id);
            return ResponseEntity.ok("Producto "+id+" eliminado");
        } catch (ProductNotExistsException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
