package com.ProjectoJava.objetos;

import com.ProjectoJava.objetos.entity.User;
import com.ProjectoJava.objetos.repository.UserRepository;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
    @org.springframework.beans.factory.annotation.Autowired
    UserRepository userRepository;
    @Value("${admin.user}")
    private String adminUser;

    @Value("${admin.pass}")
    private String adminPass;

    @PostMapping("/login-universal")
    public String loginUniversal(@RequestParam String user, @RequestParam String pass, HttpSession session) {
        System.out.println(">>> LLEGÓ EL PEDIDO AL SERVIDOR <<<");
        System.out.println("Intentando entrar con: " + user + " y pass: " + pass);
        if (adminUser.equals(user) && adminPass.equals(pass)) {
            session.setAttribute("rol", "ADMIN");
            session.setAttribute("usuarioNombre", "Admin");
            System.out.println("Es Admin! Redirigiendo...");
            return "redirect:/admin"; // Al panel de control
        }

        Optional<User> userOpt = userRepository.findByUsername(user);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(pass)) {
            User u = userOpt.get();
            session.setAttribute("rol", "CLIENTE");
            session.setAttribute("usuarioId", u.getId());
            session.setAttribute("usuarioNombre", u.getUsername());
            System.out.println("Es Cliente! Redirigiendo a test...");
            return "redirect:/client"; // Al catálogo con precios
        }
        System.out.println("Fallo el login!");
        return "redirect:/login?error=true";
    }

    @GetMapping("/login")
    public String irAlLogin() {
        return "login"; // Esto busca 'login.html' dentro de la carpeta 'templates'
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/client";
    }
}