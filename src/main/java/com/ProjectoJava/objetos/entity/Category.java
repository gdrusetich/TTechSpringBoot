package com.ProjectoJava.objetos.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    // Constructores, Getters y Setters
    public Category() {}
    public Category(String name) { this.name = name; }

    public void setName(String name) { this.name = name; }
    public Long getId() { return id; }
    public String getName() { return name; }

}