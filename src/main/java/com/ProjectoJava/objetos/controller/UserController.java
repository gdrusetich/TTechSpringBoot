package com.ProjectoJava.objetos.controller;

import com.ProjectoJava.objetos.repository.UserRepository;
import com.ProjectoJava.objetos.repository.ProductRepository;
import com.ProjectoJava.objetos.entity.User;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuarios") // Todas las rutas empezarán con /usuarios
public class UserController {

    @Autowired
    private UserRepository userRepository;
    private ProductRepository productRepository;

    // 1. Ver la lista de usuarios (solo para el admin)
    @GetMapping("/gestion")
    public String gestionarUsuarios(Model model, HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("rol"))) {
            return "redirect:/login.html";
        }
        model.addAttribute("listaUsuarios", userRepository.findAll());
        return "gestion-usuarios"; // Crearemos este HTML después
    }

    @PostMapping("/guardar")
    public ResponseEntity<String> guardarUsuario(@RequestParam String username, 
                                                @RequestParam String password, 
                                                @RequestParam String role, 
                                                HttpSession session) {
        // 1. Verificamos Seguridad
        if (!"ADMIN".equals(session.getAttribute("rol"))) {
            return ResponseEntity.status(403).body("No autorizado");
        }

        // 2. Verificamos si ya existe el nombre
        if (userRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.status(400).body("El usuario ya existe");
        }

        try {
            User usuario = new User();
            usuario.setUsername(username);
            usuario.setPassword(password);
            usuario.setRole(role);            
            userRepository.save(usuario);
            return ResponseEntity.ok("success"); 
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error en el servidor");
        }
    }

    @PostMapping("/actualizar-perfil")
    public String actualizarPerfil(@RequestParam String nuevoUser, 
                                @RequestParam String nuevoPass, 
                                HttpSession session) {        
        Long id = (Long) session.getAttribute("usuarioId");        
        
        if (id != null) {
            Optional<User> userOpt = userRepository.findById(id);
            if (userOpt.isPresent()) {
                User u = userOpt.get();
                u.setUsername(nuevoUser);
                u.setPassword(nuevoPass); 
                userRepository.save(u);
                
                // Actualizamos el nombre en la sesión para el saludo
                session.setAttribute("userName", nuevoUser);
                return "redirect:/home?actualizado=true";
            }
        }
        // Si el ID es null o no existe, lo mandamos al login
        return "redirect:/home";
    }

    @PostMapping("/editar-desde-admin")
    @ResponseBody
    public String editarDesdeAdmin(@RequestParam Long id, 
                                @RequestParam String username, 
                                @RequestParam String password, 
                                HttpSession session) {
        // Seguridad: Solo el ADMIN puede editar a otros
        if (!"ADMIN".equals(session.getAttribute("rol"))) {
            return "No autorizado";
        }

        try {
            User u = userRepository.findById(id).orElseThrow();
            u.setUsername(username);
            u.setPassword(password);
            // El role se mantiene igual
            userRepository.save(u);
            return "success";
        } catch (Exception e) {
            return "error";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, HttpSession session) {
        if ("ADMIN".equals(session.getAttribute("rol"))) {
            userRepository.deleteById(id);
        }
        return "redirect:/usuarios/gestion";
    }


    @GetMapping("/listar")
    @ResponseBody
    public List<User> listarUsuarios(HttpSession session) {
        String rolEnSesion = (String) session.getAttribute("userRole");

        if (!"ADMIN".equals(rolEnSesion)) {
            return new ArrayList<>(); 
        }
        return userRepository.findAll();
    }
}