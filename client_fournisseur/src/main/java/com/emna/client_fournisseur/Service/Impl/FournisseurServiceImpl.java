package com.emna.client_fournisseur.Service.Impl;

import com.emna.client_fournisseur.Repository.FournisseurRepository;
import com.emna.client_fournisseur.Service.FournisseurService;
import com.emna.client_fournisseur.entites.Fournisseur;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FournisseurServiceImpl implements FournisseurService {
    private final FournisseurRepository fournisseurRepository;

    public FournisseurServiceImpl(FournisseurRepository fournisseurRepository) {
        this.fournisseurRepository = fournisseurRepository;
    }

    @Override
    public void DeleteFournisseur(Integer id) {
        this.fournisseurRepository.deleteById(id);
    }

    @Override
    public Fournisseur getFournisseurByID(Integer id) {
        return this.fournisseurRepository.getById(id);
    }

    @Override
    public List<Fournisseur> getFournisseur() {
        List<Fournisseur> fournisseurs = this.fournisseurRepository.findAll();
        return fournisseurs;
    }

    @Override
    public Fournisseur updateFournisseur(Fournisseur fournisseur, Integer id) {
        Fournisseur exestingFournisseur = fournisseurRepository.getById(id);
        exestingFournisseur.setFirstName(fournisseur.getFirstName());
        exestingFournisseur.setLastName(fournisseur.getLastName());
        exestingFournisseur.setAddress(fournisseur.getAddress());
        exestingFournisseur.setEmail(fournisseur.getEmail());
        exestingFournisseur.setTelNumbers(fournisseur.getTelNumbers());
        fournisseurRepository.save(exestingFournisseur);
        return exestingFournisseur;
    }

    @Override
    public Fournisseur createFournisseur(Fournisseur fournisseur) {
        var newfournisseur = Fournisseur.builder().firstName(fournisseur.getFirstName())
                .lastName(fournisseur.getLastName())
                .email(fournisseur.getEmail())
                .address(fournisseur.getAddress())
                .telNumbers(fournisseur.getTelNumbers()).build();
        Fournisseur savedFournisseur = fournisseurRepository.save(newfournisseur);
        return savedFournisseur;
    }
}