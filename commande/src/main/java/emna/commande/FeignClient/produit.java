package emna.commande.FeignClient;


import emna.commande.DTO.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "produit", url = "http://localhost:8083") // adapte le port
public interface produit {
    @GetMapping("/product/getproduct/{id}")
    Product getProductById(@PathVariable("id") Integer id, @RequestHeader("Authorization") String token);
}


