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
    private String usuarioCorrecto;

    @Value("${admin.pass}")
    private String claveCorrecta;

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String user, @RequestParam String pass, HttpSession session) {
        if (usuarioCorrecto.equals(user) && claveCorrecta.equals(pass)) {
            session.setAttribute("usuarioLogueado", true);
            return "redirect:/admin";
        }
        return "redirect:/login.html?error=true";
    }

    @GetMapping("/admin")
    public String mostrarAdmin(HttpSession session) {
        if (session.getAttribute("usuarioLogueado") != null) {
            return "admin";
        }
        return "redirect:/login.html";
    }

    // 3. EL SALIDA: Limpia todo
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/test.html";
    }

}