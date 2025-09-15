package emna.commande.FeignClient;

import emna.commande.DTO.Fournisseur;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "fournisseur-service", url = "http://localhost:8081") // adapte le port
public interface FournisseurClient {
    @GetMapping("/client/getfournisseur/{id}")
    Fournisseur getFournisseurById(@PathVariable("id") Integer id, @RequestHeader("Authorization") String token);
}

