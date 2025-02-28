package com.emna.produit.Repository;

import com.emna.produit.Entities.Product;
import com.emna.produit.Entities.SousCategory;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    List<Product> findBySousCategory(SousCategory sousCategory);

}
