package emna.commande.dao;

import emna.commande.DTO.Product;

public class LigneCommandeResponse {
    private Product product;
    private int quantite;

    public LigneCommandeResponse() {}

    public LigneCommandeResponse(Product product, int quantite) {
        this.product = product;
        this.quantite = quantite;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }
}
