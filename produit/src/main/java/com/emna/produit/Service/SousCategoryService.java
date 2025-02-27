package com.emna.produit.Service;


import com.emna.produit.DAO.SousCategoryRequest;
import com.emna.produit.Entities.Category;
import com.emna.produit.Entities.SousCategory;

import java.util.List;

public interface SousCategoryService  {

    void deleteCategory (Integer id);
    SousCategory GetCategory (Integer id);
    List<SousCategory> GetAllCategories ();
    SousCategory CreateCategory (SousCategoryRequest request);
    SousCategory UpdateCategory (SousCategoryRequest category,Integer id);
    List<SousCategory> GetAllsousCategoriesbyid (Integer id);
}
