package emna.commande.dao;

import emna.commande.Entities.LigneCommande;

import java.time.LocalDate;
import java.util.List;

public class CommandeApproRequest {

    private Integer fournisseurId;
    private List<LigneCommande> lignesCommande;

    private double prixTotal;
    private LocalDate dateAchat;

    public CommandeApproRequest() {}

    public CommandeApproRequest(Integer fournisseurId, List<LigneCommande> lignesCommande, double prixTotal, LocalDate dateAchat) {
        this.fournisseurId = fournisseurId;
        this.lignesCommande = lignesCommande;
        this.prixTotal = prixTotal;
        this.dateAchat = dateAchat;
    }

    // Getters & Setters
    public Integer getFournisseurId() {
        return fournisseurId;
    }

    public void setFournisseurId(Integer fournisseurId) {
        this.fournisseurId = fournisseurId;
    }

    public List<LigneCommande> getLignesCommande() {
        return lignesCommande;
    }

    public void setLignesCommande(List<LigneCommande> lignesCommande) {
        this.lignesCommande = lignesCommande;
    }

    public double getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(double prixTotal) {
        this.prixTotal = prixTotal;
    }

    public LocalDate getDateAchat() {
        return dateAchat;
    }

    public void setDateAchat(LocalDate dateAchat) {
        this.dateAchat = dateAchat;
    }
}
