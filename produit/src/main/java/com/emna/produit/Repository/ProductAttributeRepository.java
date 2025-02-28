package com.emna.produit.Repository;

import com.emna.produit.Entities.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductAttributeRepository extends JpaRepository<ProductAttribute,Integer> {

    List<ProductAttribute> findByProductId(Integer productId);

}
