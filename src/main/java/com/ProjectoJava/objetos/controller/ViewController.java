package com.ProjectoJava.objetos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class ViewController {

    @GetMapping("/client")
    public String mostrarCliente(HttpSession session) {
        return "client"; 
    }

    @GetMapping("/detalle")
    public String mostrarDetalle(@RequestParam Long id) {
        return "detalle";
    }
}