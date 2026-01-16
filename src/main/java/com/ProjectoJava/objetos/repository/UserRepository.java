package com.ProjectoJava.objetos.repository;
import com.ProjectoJava.objetos.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Método clave para el login: buscar por nombre de usuario
    Optional<User> findByUsername(String username);
}