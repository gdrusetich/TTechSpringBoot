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
    public String loginUniversal(
        @RequestParam String user, 
        @RequestParam String pass, 
        @RequestParam(required = false) String redirectUrl, 
        HttpSession session) {
        
        // 1. Definimos a dónde volver
        String finalRedirect = (redirectUrl != null && !redirectUrl.isEmpty()) ? redirectUrl : "/home";

        // 2. CASO ADMIN HARCODEADO (Creamos un usuario "virtual")
        if (adminUser.equals(user) && adminPass.equals(pass)) {
            User adminVirtual = new User();
            adminVirtual.setUsername(adminUser);
            adminVirtual.setRole("ADMIN"); 
            
            session.setAttribute("usuarioLogueado", adminVirtual); // Para que el /admin te deje entrar
            session.setAttribute("rol", "ADMIN"); // Por si usás el String en otro lado
            
            return "redirect:/admin?loginSuccess=Admin";
        }

        // 3. CASO USUARIO DE BASE DE DATOS
        Optional<User> userOpt = userRepository.findByUsername(user);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(pass)) {
            User u = userOpt.get();
            String rol = u.getRole().toString();
            
            session.setAttribute("usuarioLogueado", u); // Objeto completo para el Controller
            session.setAttribute("rol", rol);
            session.setAttribute("usuarioNombre", u.getUsername());
            
            session.setAttribute("usuarioId", u.getId());
            // Si es Admin de DB, mandarlo  al panel de control
            if ("ADMIN".equals(rol)) {
                return "redirect:/admin?loginSuccess=" + u.getUsername();
            }

            // Si es usuario común, respetar el redirectUrl
            String conector = finalRedirect.contains("?") ? "&" : "?";
            return "redirect:" + finalRedirect + conector + "loginSuccess=" + u.getUsername() + "&rol=" + rol;
        }
        return "redirect:/login?error=true";
    }

    @GetMapping("/login")
    public String irAlLogin() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/home";
    }
}