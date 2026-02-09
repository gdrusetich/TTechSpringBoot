package com.ProjectoJava.objetos.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    public User() {}
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Long getId() { return id; }
    public Role getRole() { return role; }
    public void setUsername(String username) { this.username = username; }
    public void setRole(Role role) { this.role = role; }
    public void setPassword(String password) { this.password = password; }
}