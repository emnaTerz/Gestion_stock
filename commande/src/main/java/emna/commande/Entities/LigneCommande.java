package emna.commande.Entities;


import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "ligne_commande")
@Data
public class LigneCommande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer productId;

    private int quantite;

    public LigneCommande(Integer productId, int quantite) {
        this.productId = productId;
        this.quantite = quantite;
    }
    public LigneCommande() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }
}