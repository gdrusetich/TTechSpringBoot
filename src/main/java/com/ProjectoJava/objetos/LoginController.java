package com.ProjectoJava.objetos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Value("${admin.user}")
    private String adminUser;

    @Value("${admin.pass}")
    private String adminPass;

    // --- LOGIN DE ADMINISTRADOR ---
    @PostMapping("/login-admin")
    public String loginAdmin(@RequestParam String user, @RequestParam String pass, HttpSession session) {
        if (adminUser.equals(user) && adminPass.equals(pass)) {
            session.setAttribute("rol", "ADMIN");
            return "redirect:/admin";
        }
        return "redirect:/login-admin.html?error=true";
    }

    // --- LOGIN DE CLIENTES (Usuarios comunes) ---
    @PostMapping("/login-cliente")
    public String loginCliente(@RequestParam String user, @RequestParam String pass, HttpSession session) {
        // Aquí luego conectarás con tu base de datos de clientes
        // Por ahora, un ejemplo simple:
        if ("cliente".equals(user) && "123".equals(pass)) {
            session.setAttribute("rol", "CLIENTE");
            return "redirect:/test.html"; // Al loguearse, vuelve a ver los precios
        }
        return "redirect:/login-cliente.html?error=true";
    }

    // --- RUTAS DE ACCESO ---

    @GetMapping("/admin")
    public String mostrarAdmin(HttpSession session) {
        if ("ADMIN".equals(session.getAttribute("rol"))) {
            return "admin";
        }
        return "redirect:/login-admin.html";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/test.html";
    }
}