package com.ProjectoJava.objetos;

import com.ProjectoJava.objetos.entity.User;
import com.ProjectoJava.objetos.entity.Role;
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
        
        String finalRedirect = (redirectUrl != null && !redirectUrl.isEmpty()) ? redirectUrl : "/home";

        if (adminUser.equals(user) && adminPass.equals(pass)) {
            User adminVirtual = new User();
            adminVirtual.setUsername(adminUser);
            adminVirtual.setRole(Role.ADMIN);
            session.setAttribute("userRole", Role.ADMIN.name());            
            session.setAttribute("userLogger", adminVirtual);
            session.setAttribute("userName", adminUser); // Clave para el saludo
            session.setAttribute("userRole", "ADMIN");      // Clave para el th:if
            
            return "redirect:/admin?loginSuccess=Admin";
        }

        Optional<User> userOpt = userRepository.findByUsername(user);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(pass)) {
            User u = userOpt.get();
            String rol = u.getRole().toString();
            
            session.setAttribute("userLogger", u); // Objeto completo para el Controller
            session.setAttribute("userRole", rol);
            session.setAttribute("userName", u.getUsername());
            
            session.setAttribute("userId", u.getId());
            // Si es Admin de DB, mandarlo  al panel de control
            if (u.getRole().name().equals("ADMIN")) {
                return "redirect:/admin?loginSuccess=" + u.getUsername();
            }

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