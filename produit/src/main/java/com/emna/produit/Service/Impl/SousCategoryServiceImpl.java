package com.emna.produit.Service.Impl;

import com.emna.produit.DAO.SousCategoryRequest;
import com.emna.produit.Entities.Category;
import com.emna.produit.Entities.SousCategory;
import com.emna.produit.Repository.SousCategorieRepository;
import com.emna.produit.Service.CategoryService;
import com.emna.produit.Service.SousCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SousCategoryServiceImpl implements SousCategoryService {


    private final SousCategorieRepository sousCategorieRepository;
    private final CategoryService categoryService;

    public SousCategoryServiceImpl(SousCategorieRepository sousCategorieRepository, CategoryService categoryService) {
        this.sousCategorieRepository = sousCategorieRepository;
        this.categoryService = categoryService;
    }

    @Override
    public void deleteCategory(Integer id) {

        SousCategory exestingSous = sousCategorieRepository.getById(id);
        if (exestingSous != null) {

            sousCategorieRepository.deleteById(id);
        }

    }

    @Override
    public SousCategory GetCategory(Integer id) {
        return sousCategorieRepository.getById(id);
    }

    @Override
    public List<SousCategory> GetAllCategories() {
        return sousCategorieRepository.findAll();
    }

    @Override
    public SousCategory CreateCategory(SousCategoryRequest request) {
        Category category = categoryService.GetCategory(request.getCategoryId());
             SousCategory newsous = new SousCategory(request.getName(), request.getImageUrl(),category);
              return sousCategorieRepository.save(newsous);
    }

    @Override
    public SousCategory UpdateCategory(SousCategoryRequest request, Integer id) {
        Category category = categoryService.GetCategory(request.getCategoryId());
        SousCategory newsous = new SousCategory(request.getName(), request.getImageUrl(), category);
        newsous.setName(request.getName());
        newsous.setImageUrl(request.getImageUrl());

        return sousCategorieRepository.save(newsous);

    }

    @Override
    public List<SousCategory> GetAllsousCategoriesbyid(Integer id) {
        return sousCategorieRepository.findByCategory_Id(id);
    }


}
