package com.ProjectoJava.objetos.entity;

import jakarta.persistence.*;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @OnDelete(action = OnDeleteAction.CASCADE) // Si borras el padre, se borran los hijos
    private Category parent;

    public Category() {}
    public Category(String name) { this.name = name; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setParent(Category parent){this.parent = parent;}
    public Long getId() { return id; }
    public String getName() { return name; }
    public Category getParent(){return parent;}

}