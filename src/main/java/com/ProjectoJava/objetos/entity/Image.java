package com.ProjectoJava.objetos.entity;
import jakarta.persistence.*;

@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    
    public Long getId(){return this.id;}
    public String getUrl(){return this.url;}
    public Product getProduct(){return this.product;}
    public void setUrl(String unaUrl){this.url = unaUrl;}
    public void setProduct(Product unProducto){this.product = unProducto;}
}
