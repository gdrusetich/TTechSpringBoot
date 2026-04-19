package com.ProjectoJava.objetos.controller;

import com.ProjectoJava.objetos.DTO.response.ProductResponseDTO;
import com.ProjectoJava.objetos.entity.Product;
import com.ProjectoJava.objetos.entity.Category;
import com.ProjectoJava.objetos.entity.User;
import com.ProjectoJava.objetos.entity.Role;

import com.ProjectoJava.objetos.repository.CategoryRepository;
import com.ProjectoJava.objetos.repository.ProductRepository;
import com.ProjectoJava.objetos.repository.UserRepository;

import com.ProjectoJava.objetos.service.FeaturedProductService;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
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
    @Autowired
    FeaturedProductService featuredService;

    @GetMapping("/detalle") 
    public String verDetalle(@RequestParam("id") Long id, Model model, HttpSession session) {
        Product producto = productRepository.findById(id).orElse(null);
        if (producto == null) {
            return "redirect:/";
        }
        ProductResponseDTO productoDTO = new ProductResponseDTO(producto);
        model.addAttribute("producto", productoDTO);
        model.addAttribute("rolActual", session.getAttribute("rol"));
        return "detalle"; 
    }
    
    @GetMapping("/dinastia")
    public String verNosotros() {
        return "dinastia"; // Esto busca el archivo dinastia.html
    }

    @GetMapping("/home")
    public String mostrarHome(HttpSession session, Model model) {
        var destacados = featuredService.getAllFeaturedDTOs();
        model.addAttribute("destacados", destacados);

        return "home"; 
    }

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        return mostrarHome(session, model);
    }

    @GetMapping("/perfil")
    public String irAlPerfil(HttpSession session) {
        if (session.getAttribute("userName") == null) {
            return "redirect:/home"; // Si no está logueado, fuera
        }
        return "perfil"; // Esto carga perfil.html
    }

    @GetMapping("/products/prices") // Podés dejar la ruta así para que coincida con tu botón
    public String editarPrecios(HttpSession session) {
        User usuario = (User) session.getAttribute("userLogger");

        if (usuario == null || usuario.getRole() != Role.ADMIN) {
            return "redirect:/home"; 
        }
        
        return "prices"; // Busca templates/prices.html
    }

    @GetMapping("/admin")
    public String vistaAdmin(HttpSession session, Model model) {
        User usuario = (User) session.getAttribute("userLogger");
        if (usuario == null || usuario.getRole() != Role.ADMIN) {
            return "redirect:/home"; 
        }
        List<Category> padres = categoryRepository.findByParentIsNull();
        padres.sort(Comparator.comparing(Category::getName, 
                    Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)));
        model.addAttribute("categoriasPadre", padres);
        return "admin"; 
    }

    @GetMapping("/detalle-admin")
    public String detalleAdmin() {
        return "detalleadmin"; 
    }

    @GetMapping("/actualizar")
    public String verPaginaActualizar(HttpSession session) {
        Object roleAttr = session.getAttribute("userRole"); 
        if (roleAttr == null || !roleAttr.toString().equals(Role.ADMIN.name())) {
            return "redirect:/login"; 
        }
        
        return "actualizar";
    }
}