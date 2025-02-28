package com.emna.produit.Service;

import com.emna.produit.DAO.ProductResponse;
import com.emna.produit.DAO.ProductResquest;
import com.emna.produit.Entities.Product;
import com.emna.produit.Entities.SousCategory;
import org.springframework.stereotype.Service;

import java.util.List;
public interface ProductService {

    void deleteProduct (Integer id);
    ProductResponse getProduct (Integer id);
    List <ProductResponse> getProducts ();
    Product createProduct (ProductResquest productResquest);
    Product updateProduct (ProductResquest productResquest,Integer id);
    List <ProductResponse> getProductsBySousCategory (SousCategory sousCategory);

}
