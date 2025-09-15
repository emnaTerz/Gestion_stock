package com.emna.produit.Service.Impl;

import com.emna.produit.DAO.ProductAttributeResponse;
import com.emna.produit.DAO.ProductResponse;
import com.emna.produit.DAO.ProductResquest;
import com.emna.produit.Entities.Product;
import com.emna.produit.Entities.ProductAttribute;
import com.emna.produit.Entities.SousCategory;
import com.emna.produit.Repository.ProductAttributeRepository;
import com.emna.produit.Repository.ProductRepository;
import com.emna.produit.Service.ProductService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {


    private final ProductRepository productRepository;
    private final ProductAttributeRepository productAttributeRepository;

    public ProductServiceImpl(ProductRepository productRepository, ProductAttributeRepository productAttributeRepository) {
        this.productRepository = productRepository;
        this.productAttributeRepository = productAttributeRepository;
    }

    @Override
    public void deleteProduct(Integer id) {
        Product existingproduct = productRepository.getById(id);
        if (existingproduct != null) {productRepository.delete(existingproduct);}

    }


    @Override
    public ProductResponse getProduct(Integer id) {
        // Récupérer le produit par ID
        Product product = productRepository.getById(id);

        // Récupérer les attributs du produit
        List<ProductAttribute> productAttributes = productAttributeRepository.findByProductId(product.getId());

        // Convertir les ProductAttributes en ProductAttributeResponse
        List<ProductAttributeResponse> productAttributeResponses = productAttributes.stream()
                .map(pa -> new ProductAttributeResponse(
                        pa.getAttribute().getName(),  // Nom de l'attribut
                        pa.getValue()  // Valeur de l'attribut
                ))
                .collect(Collectors.toList());

        // Créer un ProductResponse avec les attributs transformés
        ProductResponse productResponse = new ProductResponse(
                product.getId(),
                product.getName(),
                product.getImageUrl(),
                product.getPrice(),
                product.getMarque(),
                productAttributeResponses,
                product.getSousCategory(),
                product.getQuantité()

                );
System.out.println("sous cat   " +   product.getSousCategory().getName()
);
        return productResponse;
    }


    @Override
    public List<ProductResponse> getProducts() {
        // Récupérer tous les produits
        List<Product> products = productRepository.findAll();

        List<ProductResponse> productResponses = new ArrayList<>();

        for (Product product : products) {
            // Récupérer les attributs pour chaque produit
            List<ProductAttribute> productAttributes = productAttributeRepository.findByProductId(product.getId());

            // Convertir les ProductAttributes en ProductAttributeResponse
            List<ProductAttributeResponse> productAttributeResponses = productAttributes.stream()
                    .map(pa -> new ProductAttributeResponse(
                            pa.getAttribute().getName(),  // Nom de l'attribut
                            pa.getValue()  // Valeur de l'attribut
                    ))
                    .collect(Collectors.toList());

            // Créer un ProductResponse avec ses attributs
            ProductResponse productResponse = new ProductResponse(
                    product.getId(),
                    product.getName(),
                    product.getImageUrl(),
                    product.getPrice(),
                    product.getMarque(),
                    productAttributeResponses,
                    product.getSousCategory(),
                    product.getQuantité()
                    );

            productResponses.add(productResponse);
        }

        return productResponses;
    }

    @Override
    public Product createProduct(ProductResquest productResquest) {
        Product newProduct = new Product(productResquest.getName(), productResquest.getImageUrl(),
                productResquest.getPrice(),productResquest.getQuantité(),productResquest.getMarque(),productResquest.getSousCategory());

        Product savedProduct = productRepository.save(newProduct);
        if (productResquest.getAttributes() != null && !productResquest.getAttributes().isEmpty()) {
            List<ProductAttribute> productAttributes = productResquest.getAttributes().stream()
                    .map(attr -> new ProductAttribute(savedProduct, attr.getAttribute(), attr.getValue()))
                    .collect(Collectors.toList());

            productAttributeRepository.saveAll(productAttributes);
        }

        return savedProduct;

    }

    @Override
    public Product updateProduct(ProductResquest productResquest, Integer id) {
        Product existingproduct = productRepository.getById(id);
        if (existingproduct != null) {
            existingproduct.setName(productResquest.getName());
            existingproduct.setImageUrl(productResquest.getImageUrl());
            existingproduct.setMarque(productResquest.getMarque());
            existingproduct.setPrice(productResquest.getPrice());
            existingproduct.setSousCategory(productResquest.getSousCategory());
        }
        return productRepository.save(existingproduct);
    }



    @Override
    public List<ProductResponse> getProductsBySousCategory(SousCategory sousCategory) {
        // Récupérer tous les produits de la sous-catégorie
        List<Product> products = productRepository.findBySousCategory(sousCategory);

        List<ProductResponse> productResponses = new ArrayList<>();

        for (Product product : products) {
            // Récupérer les attributs pour chaque produit
            List<ProductAttribute> productAttributes = productAttributeRepository.findByProductId(product.getId());

            // Convertir les ProductAttributes en ProductAttributeResponse
            List<ProductAttributeResponse> productAttributeResponses = productAttributes.stream()
                    .map(pa -> new ProductAttributeResponse(
                            pa.getAttribute().getName(),  // Nom de l'attribut
                            pa.getValue()  // Valeur de l'attribut
                    ))
                    .collect(Collectors.toList());

            ProductResponse productResponse = new ProductResponse(
                    product.getId(),
                    product.getName(),
                    product.getImageUrl(),
                    product.getPrice(),
                    product.getMarque(),
                    productAttributeResponses,
                    product.getSousCategory(),
                    product.getQuantité()

                    );

            productResponses.add(productResponse);
        }

        return productResponses;
    }

}
