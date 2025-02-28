package com.emna.produit.Entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Entity
@Table(name = "Product")  // Ensure table name is valid
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @Column(unique = true)
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime creationDate;
    private String imageUrl;
    private double price;
    private String marque;
    @ManyToOne
    @JoinColumn(name = "sous_category_id", nullable = false)  // Clé étrangère vers Category
    private SousCategory sousCategory;


    public Product(String name, String imageUrl, double price, String marque, SousCategory sousCategory) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.marque = marque;
        this.sousCategory = sousCategory;
        this.creationDate = LocalDateTime.now();
    }

    public Product() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public SousCategory getSousCategory() {
        return sousCategory;
    }

    public void setSousCategory(SousCategory sousCategory) {
        this.sousCategory = sousCategory;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }
}
