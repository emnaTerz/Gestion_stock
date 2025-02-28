package com.emna.produit.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@Entity
@Table(name = "category")
public class Category {  // Correct class naming convention

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @Column(unique = true)
    private String name;

    @Temporal(TemporalType.TIMESTAMP)  // Correct annotation for date
    private LocalDateTime creationDate;
    private String imageUrl;


    public Category(String name, String imageUrl) {
        this.name = name;
        this.creationDate = LocalDateTime.now();
        this.imageUrl = imageUrl;
    }
    public Category() {

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
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}

