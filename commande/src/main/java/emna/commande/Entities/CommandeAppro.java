package emna.commande.Entities;

import emna.commande.DTO.Fournisseur;
import emna.commande.DTO.Product;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;



@Data
@Builder
@Entity
@Table(name = "CommandeAppro")
public class CommandeAppro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fournisseur_id")
    private Integer fournisseurId; // Persisté en base

    @Transient
    private Fournisseur fournisseur;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "commande_id") // <- clé étrangère sera ajoutée dans LigneCommandeAppro mais pas mappée en Java
    private List<LigneCommande> lignesCommande;
    private double prixTotal;
    private LocalDate dateAchat;


    // Constructeurs
    public CommandeAppro() {}

    public CommandeAppro(Fournisseur fournisseur, List<LigneCommande> lignesCommande, double prixTotal) {
        this.fournisseurId = fournisseur.getId();
        this.fournisseur = fournisseur;
        this.lignesCommande = lignesCommande;
        this.prixTotal = prixTotal;
        this.dateAchat = LocalDate.now();
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFournisseurId() {
        return fournisseurId;
    }

    public void setFournisseurId(Integer fournisseurId) {
        this.fournisseurId = fournisseurId;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
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
