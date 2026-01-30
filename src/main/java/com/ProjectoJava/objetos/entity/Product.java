package com.ProjectoJava.objetos.entity;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;


@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProducto;

    @Column(nullable = false, unique = true)
    private String title;

    private double price;
    private int stock;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToMany
    @JoinTable(
        name = "product_categories",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();
    
    public Product(){}

    public Long getId(){return this.idProducto;}
    public String getTitle(){return this.title;}
    public double getPrice( ){return this.price;}
    public int getStock( ){return this.stock;}
    public String getDescription(){return this.description;}
    public Set<Category> getCategories(){return this.categories;}
    public List<Image> getImages(){return this.images;}

    public void setTitle(String newTitle){this.title = newTitle;}
    public void setPrice(double newPrice){this.price = newPrice;}
    public void setStock(int nuevoStock){this.stock = nuevoStock;}
    public void setDescription(String nuevaDescripcion){this.description = nuevaDescripcion;}
    public void setCategories(Set<Category> nuevaCategoria){this.categories = nuevaCategoria;}
    public void setImageURL(List<Image> nuevaImagen){this.images  = nuevaImagen;}
    public void addCategory(Category unCategory){this.categories.add(unCategory);}
    public void addImage(Image unaImagen){this.images.add(unaImagen);}
    double precioConDescuento(double descuento){
        return price - price * descuento;
    }


}