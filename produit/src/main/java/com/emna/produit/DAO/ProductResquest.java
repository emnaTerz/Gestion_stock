package com.emna.produit.DAO;

import com.emna.produit.Entities.Category;
import com.emna.produit.Entities.ProductAttribute;
import com.emna.produit.Entities.SousCategory;

import java.util.List;


public class ProductResquest {
    private String name;
    private String imageUrl;
    private SousCategory sousCategory;
    private double price;
    private String marque;
private List<ProductAttribute> attributes;

    public List<ProductAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<ProductAttribute> attributes) {
        this.attributes = attributes;
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
