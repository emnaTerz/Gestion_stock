package com.emna.produit.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;



@Data
@Builder
@AllArgsConstructor
@Entity
@Table(name = "sous-category")  // Ensure table name is valid

public class SousCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @Column(unique = true)
    private String name;
    @Temporal(TemporalType.TIMESTAMP)  // Correct annotation for date
    private LocalDateTime creationDate;
    private String imageUrl;  // Attribut pour l'URL de l'image

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    public SousCategory(String name, String imageUrl, Category category_Id) {
        this.name = name;
        this.creationDate = LocalDateTime.now();
        this.imageUrl = imageUrl;
        this.category = category_Id;
    }


    public SousCategory() {

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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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
}