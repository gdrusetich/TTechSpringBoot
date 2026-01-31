package com.ProjectoJava.objetos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class ViewController {

    @GetMapping("/home")
    public String mostrarhomee(HttpSession session) {
        return "home"; 
    }

    // SOLO UNO para detalle, y sin parámetros
    @GetMapping("/detalle")
    public String detalle() {
        return "detalle"; 
    }

    @GetMapping("/perfil")
    public String perfil() {
        return "perfil"; 
    }



    @GetMapping("/admin")
    public String admin() {
        return "admin"; 
    }

    @GetMapping("/detalle-admin")
    public String detalleAdmin() {
        return "detalleadmin"; 
    }
}