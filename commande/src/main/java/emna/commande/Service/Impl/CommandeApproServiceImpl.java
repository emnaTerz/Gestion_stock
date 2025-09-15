package emna.commande.Service.Impl;


import emna.commande.Entities.LigneCommande;
import emna.commande.FeignClient.FournisseurClient;
import emna.commande.FeignClient.produit;
import emna.commande.dao.CommandeApproResponse;
import emna.commande.DTO.Fournisseur;
import emna.commande.DTO.Product;
import emna.commande.Entities.CommandeAppro;
import emna.commande.Repository.CommandeApproRepository;
import emna.commande.dao.CommandeApproRequest;
import emna.commande.Service.CommandeApproService;
import emna.commande.dao.LigneCommandeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommandeApproServiceImpl implements CommandeApproService {
    @Autowired
    private FournisseurClient fournisseurClient;

    @Autowired
    private produit produitClient;
    @Autowired
    private CommandeApproRepository repository;

    @Override
    public CommandeApproResponse save(CommandeApproRequest request, String token) {
        String bearerToken = "Bearer " + token;

        // 1. Récupérer le fournisseur
        Fournisseur fournisseur = fournisseurClient.getFournisseurById(request.getFournisseurId(), bearerToken);

        // 2. Calculer le prix total
        double prixTotal = request.getLignesCommande().stream()
                .mapToDouble(ligne -> {
                    Product produit = produitClient.getProductById(ligne.getProductId(), bearerToken);
                    return produit.getPrice() * ligne.getQuantite();
                })
                .sum();

        // 3. Construire la commande
        CommandeAppro commande = new CommandeAppro(fournisseur, request.getLignesCommande(), prixTotal);


        // 4. Sauvegarder
        CommandeAppro saved = repository.save(commande);

        // 5. Retourner la réponse en passant le token également à mapToResponse
        return mapToResponse(saved, token);
    }



    @Override
    public CommandeApproResponse getById(Integer id, String token) {
        CommandeAppro commande = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande not found"));

        String bearerToken = "Bearer " + token;

        // Récupération des infos fournisseur
        Fournisseur fournisseur = fournisseurClient.getFournisseurById(commande.getFournisseurId(), bearerToken);
        commande.setFournisseur(fournisseur);

        return mapToResponse(commande, token);
    }


    @Override
    public List<CommandeApproResponse> getAll() {
        return null;
    }


    @Override
    public CommandeApproResponse update(Integer id, CommandeApproRequest request) {
        return null;
    }

 /*   @Override
    public CommandeApproResponse update(Integer id, CommandeApproRequest request) {
        Optional<CommandeAppro> optional = repository.findById(id);
        if (optional.isPresent()) {
            CommandeAppro commande = optional.get();

            // 1. Récupérer le fournisseur
            Fournisseur fournisseur = fournisseurClient.getFournisseurById(request.getFournisseurId());

            // 2. Récupérer les produits
            List<Product> produits = request.getProduitIds().stream()
                    .map(produitClient::getProductById)
                    .collect(Collectors.toList());

            // 3. Recalculer le prix total
            double prixTotal = produits.stream()
                    .mapToDouble(p -> p.getPrice() * p.getQuantité())
                    .sum();

            // 4. Mettre à jour les champs
            commande.setFournisseurId(fournisseur);
            commande.setProduits(produits);
            commande.setPrixTotal(prixTotal);
            commande.setDateAchat(request.getDateAchat());

            CommandeAppro updated = repository.save(commande);
            return mapToResponse(updated);
        }
        return null;
    }
*/

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    private CommandeApproResponse mapToResponse(CommandeAppro commande, String token) {
        String bearerToken = "Bearer " + token;

        // Convertir LigneCommande -> LigneCommandeResponse avec détails produits
        List<LigneCommandeResponse> lignes = commande.getLignesCommande().stream()
                .map(ligne -> {
                    // Récupérer le produit par ID et ajouter les détails du produit
                    Product produit = produitClient.getProductById(ligne.getProductId(), bearerToken);
                    // Retourner une nouvelle instance de LigneCommandeResponse avec le produit et la quantité
                    return new LigneCommandeResponse(produit, ligne.getQuantite());
                })
                .collect(Collectors.toList());

        // Retourner la réponse de la commande avec les lignes de commande enrichies
        return new CommandeApproResponse(
                commande.getId(),
                commande.getFournisseur(),
                lignes,
                commande.getPrixTotal(),
                commande.getDateAchat()
        );
    }


}
