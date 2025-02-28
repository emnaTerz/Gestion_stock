package com.emna.produit.DAO;

import com.emna.produit.Entities.ProductAttribute;

import com.emna.produit.Entities.SousCategory;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ProductResponse {
    private Integer id;
    private String name;
    private String imageUrl;  // Optionnel si tu veux l'ajouter
    private Double price;  // Exemple d'attribut
    private String marque;
    @JsonProperty("sousCategory")
    private SousCategory sousCategory;
    private List<ProductAttributeResponse> attributes;

    public ProductResponse(Integer id, String name, String imageUrl, Double price, String marque, List<ProductAttributeResponse> attributes,SousCategory sousCategory) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.marque = marque;
        this.attributes = attributes;
        this.sousCategory = sousCategory;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<ProductAttributeResponse> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<ProductAttributeResponse> attributes) {
        this.attributes = attributes;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }
}
