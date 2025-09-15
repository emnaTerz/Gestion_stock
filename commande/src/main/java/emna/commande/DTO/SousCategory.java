package emna.commande.DTO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;



@Data
public class SousCategory {

    private String name;
    private LocalDateTime creationDate;
    private String imageUrl;  // Attribut pour l'URL de l'image
    private Category category;
    public SousCategory(String name, String imageUrl, Category category_Id) {
        this.name = name;
        this.creationDate = LocalDateTime.now();
        this.imageUrl = imageUrl;
        this.category = category_Id;
    }


    public SousCategory() {

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