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
        @RequestParam(required = false) String redirectUrl, // <-- Recibimos la URL de retorno
        HttpSession session) {
        
        String finalRedirect = (redirectUrl != null && !redirectUrl.isEmpty()) ? redirectUrl : "/home";
        
        // Si es Admin
        if (adminUser.equals(user) && adminPass.equals(pass)) {
            session.setAttribute("rol", "ADMIN");
            return "redirect:/admin?loginSuccess=Admin";
        }

        // Si es homee
        Optional<User> userOpt = userRepository.findByUsername(user);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(pass)) {
            User u = userOpt.get();
            String rol = u.getRole().toString(); // Asumiendo que tenés un campo role
            
            session.setAttribute("rol", rol);
            session.setAttribute("usuarioNombre", u.getUsername());
            
            // Agregamos el parámetro a la URL de destino
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