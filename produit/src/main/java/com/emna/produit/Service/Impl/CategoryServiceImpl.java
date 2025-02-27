package com.emna.produit.Service.Impl;

import com.emna.produit.DAO.CategoryResquest;
import com.emna.produit.Entities.Category;
import com.emna.produit.Repository.CategorieRepository;
import com.emna.produit.Service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategorieRepository categorieRepository;

    public CategoryServiceImpl(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

    @Override
    public void deleteCategory(Integer id) {
        Category existingCat = categorieRepository.getById(id);
      if (existingCat != null)  {categorieRepository.deleteById(id);}
    }

    @Override
    public Category GetCategory(Integer id) {
        return  categorieRepository.getById(id);

    }

    @Override
    public List<Category> GetAllCategories() {
      return categorieRepository.findAll();
    }

    @Override
    public   Category CreateCategory (CategoryResquest request)
    {
        Category newCategory = new Category(request.getName(),request.getImageUrl());
        return categorieRepository.save(newCategory);

    }

    @Override
    public Category UpdateCategory(CategoryResquest category,Integer id) {
        System.out.println("la nouvelle categorie au debut du service " + "  " + category.getName());
    Category existingcateg = categorieRepository.getById(id);
        existingcateg.setName(category.getName());
        existingcateg.setImageUrl(category.getImageUrl());
        System.out.println("la nouvelle categorie dans service " + "  " + existingcateg.getName());
        categorieRepository.save(existingcateg);
        return existingcateg;
    }



}
