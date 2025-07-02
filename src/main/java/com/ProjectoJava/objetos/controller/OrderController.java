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

    @PostMapping
    public ResponseEntity<Order> crear(@RequestBody ArrayList<OrderLine> items) throws ProductNotExistsException, NoStockException {
        try {
            Order nueva = service.crearPedido(items);
            return ResponseEntity.ok(nueva);
        } catch (ProductNotExistsException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ArrayList<Order> listar() {
        return service.listarPedidos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> buscar(@PathVariable int id) {
        try {
            return ResponseEntity.ok(service.buscarPedido(id));
        } catch (ProductNotExistsException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

