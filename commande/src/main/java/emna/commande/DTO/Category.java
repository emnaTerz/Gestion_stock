package emna.commande.DTO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;

@Data
public class Category {  // Correct class naming convention
    private String name;
    private LocalDateTime creationDate;
    private String imageUrl;


    public Category(String name, String imageUrl) {
        this.name = name;
        this.creationDate = LocalDateTime.now();
        this.imageUrl = imageUrl;
    }
    public Category() {

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

