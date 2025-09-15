package emna.commande.dao;


import emna.commande.DTO.Fournisseur;
import emna.commande.DTO.Product;
import emna.commande.Entities.LigneCommande;

import java.time.LocalDate;
import java.util.List;

public class CommandeApproResponse {

    private Integer id;
    private Fournisseur fournisseur;
    private List<LigneCommandeResponse> lignesCommande;
    private double prixTotal;
    private LocalDate dateAchat;

    public CommandeApproResponse() {}

    public CommandeApproResponse(Integer id, Fournisseur fournisseur, List<LigneCommandeResponse> lignesCommande, double prixTotal, LocalDate dateAchat) {
        this.id = id;
        this.fournisseur = fournisseur;
        this.lignesCommande = lignesCommande;
        this.prixTotal = prixTotal;
        this.dateAchat = dateAchat;
    }

    // Getters & Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public List<LigneCommandeResponse> getLignesCommande() {
        return lignesCommande;
    }

    public void setLignesCommande(List<LigneCommandeResponse> lignesCommande) {
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
