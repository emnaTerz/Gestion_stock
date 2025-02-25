package com.emna.produit.Service;

import com.emna.produit.DAO.CategoryResquest;
import com.emna.produit.Entities.Category;

import java.util.List;

public interface CategoryService {

     void deleteCategory (Integer id);
     Category GetCategory (Integer id);
     List <Category> GetAllCategories ();
     Category CreateCategory (CategoryResquest request);
     Category UpdateCategory (Category category);

}
