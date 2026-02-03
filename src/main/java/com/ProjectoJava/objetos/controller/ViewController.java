package com.ProjectoJava.objetos.controller;

import com.ProjectoJava.objetos.DTO.response.ProductResponseDTO;
import com.ProjectoJava.objetos.entity.Product;
import com.ProjectoJava.objetos.entity.User;

import com.ProjectoJava.objetos.repository.CategoryRepository;
import com.ProjectoJava.objetos.repository.ProductRepository;
import com.ProjectoJava.objetos.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

    @Controller
    public class ViewController {

        @Autowired
        CategoryRepository categoryRepository;
        @Autowired
        ProductRepository productRepository;
        @Autowired
        UserRepository userRepository;

        @GetMapping("/detalle") 
        public String verDetalle(@RequestParam("id") Long id, Model model, HttpSession session) {
        
        Product producto = productRepository.findById(id).orElse(null);
        
        if (producto == null) {
            return "redirect:/products/list"; 
        }

        ProductResponseDTO productoDTO = new ProductResponseDTO(producto);

        model.addAttribute("producto", productoDTO);
        model.addAttribute("rolActual", session.getAttribute("rol"));

        return "detalle"; 
    }

    @GetMapping("/home")
    public String mostrarHome(HttpSession session) {
        return "home"; 
    }


    @GetMapping("/perfil")
    public String irAlPerfil(HttpSession session) {
        if (session.getAttribute("userName") == null) {
            return "redirect:/home"; // Si no está logueado, fuera
        }
        return "perfil"; // Esto carga perfil.html
    }

    @GetMapping("/admin")
    public String vistaAdmin(HttpSession session, Model model) {
        User usuario = (User) session.getAttribute("userLogger");

        if (usuario == null || !usuario.getRole().equals("ADMIN")) {
            return "redirect:/home"; 
        }
        model.addAttribute("categoriasPadre", categoryRepository.findByParentIsNull());
        return "admin"; 
    }

    @GetMapping("/detalle-admin")
    public String detalleAdmin() {
        return "detalleadmin"; 
    }
}