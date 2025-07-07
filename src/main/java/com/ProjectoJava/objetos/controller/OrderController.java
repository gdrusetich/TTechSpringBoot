package com.ProjectoJava.objetos.controller;
import exceptions.NoStockException;
import exceptions.ProductNotExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ProjectoJava.objetos.service.OrderService;
import com.ProjectoJava.objetos.entity.OrderLine;
import com.ProjectoJava.objetos.entity.Order;


import java.util.ArrayList;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    @Autowired
    public OrderController(OrderService orderService) {
        this.service = orderService;
    }

    @PostMapping("/")
    public ResponseEntity<?> crear(@RequestBody ArrayList<OrderLine> items) throws ProductNotExistsException, NoStockException {
        try {
            Order nueva = service.crearPedido(items);
            return ResponseEntity.ok(nueva);
        } catch (ProductNotExistsException | NoStockException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public ArrayList<Order> listar() {
        return service.listarPedidos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> buscar(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.buscarPedido(id));
        } catch (ProductNotExistsException e) {
            return ResponseEntity.notFound().build();
        }
    }

}

