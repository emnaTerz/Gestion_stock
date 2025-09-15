package emna.commande.DTO;

import jakarta.persistence.*;
import lombok.Data;

@Data
public class Attribute {

    private String name;

    public Attribute(String name) {
        this.name = name;
    }
    public Attribute() {

    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
