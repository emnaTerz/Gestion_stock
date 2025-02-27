package com.emna.produit.DAO;

public class SousCategoryRequest {
    private String name;  // Follow Java variable naming conventions

    private String imageUrl;
    private Integer categoryId;
    public String getName() {
        return name;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
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
