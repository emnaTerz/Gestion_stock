package com.emna.client_fournisseur.Service;

import com.emna.client_fournisseur.entites.Fournisseur;

import java.util.List;

public interface FournisseurService {

    void DeleteFournisseur (Integer id);
    Fournisseur getFournisseurByID (Integer id);

    List <Fournisseur> getFournisseur();

    Fournisseur updateFournisseur (Fournisseur fournisseur, Integer id);
    Fournisseur createFournisseur (Fournisseur fournisseur);

}
