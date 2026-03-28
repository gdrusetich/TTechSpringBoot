package com.ProjectoJava.objetos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "featured_products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeaturedProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

    @Column(nullable = false)
    private Integer position;

    public FeaturedProduct(Product product, Integer position) {
        this.product = product;
        this.position = position;
    }
}