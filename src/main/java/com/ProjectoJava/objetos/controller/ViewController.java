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

    // SOLO UNO para detalle, y sin parámetros
    @GetMapping("/detalle")
    public String detalle() {
        return "detalle"; 
    }

    @GetMapping("/admin")
    public String admin() {
        return "test"; 
    }

    @GetMapping("/detalle-admin")
    public String detalleAdmin() {
        return "detalleadmin"; 
    }
}