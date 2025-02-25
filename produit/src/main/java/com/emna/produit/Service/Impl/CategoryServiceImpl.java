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

    }

    @Override
    public Category GetCategory(Integer id) {
        return null;
    }

    @Override
    public List<Category> GetAllCategories() {
        return null;
    }

    @Override
    public   Category CreateCategory (CategoryResquest request)
    {
        Category newCategory = new Category(request.getName(),request.getImageUrl());
        return categorieRepository.save(newCategory);

    }

    @Override
    public Category UpdateCategory(Category category) {
        return null;
    }
}
