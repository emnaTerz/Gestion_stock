package com.emna.produit.Repository;

import com.emna.produit.Entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategorieRepository extends JpaRepository<Category, Integer> {
}
