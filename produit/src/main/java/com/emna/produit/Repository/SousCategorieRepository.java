package com.emna.produit.Repository;


import com.emna.produit.Entities.SousCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SousCategorieRepository extends JpaRepository<SousCategory, Integer> {
    List<SousCategory> findByCategory_Id(Integer categoryId);
}


