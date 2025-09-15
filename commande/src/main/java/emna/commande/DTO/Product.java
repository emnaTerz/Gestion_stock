package emna.commande.DTO;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Product {

    private String name;
    private LocalDateTime creationDate;
    private String imageUrl;
    private double price;
    private double quantité;
    private String marque;
    private SousCategory sousCategory;


    public Product(String name, String imageUrl, double price, String marque, SousCategory sousCategory, double quantité) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.marque = marque;
        this.sousCategory = sousCategory;
        this.creationDate = LocalDateTime.now();
        this.quantité = quantité;
    }

    public Product() {

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

    public double getQuantité() {
        return quantité;
    }

    public void setQuantité(double quantité) {
        this.quantité = quantité;
    }
}
