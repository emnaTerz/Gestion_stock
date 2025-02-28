package com.emna.produit.Entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "Attribute")
public class Attribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    public Attribute(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Attribute(String name) {
        this.name = name;
    }
    public Attribute() {

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
}
