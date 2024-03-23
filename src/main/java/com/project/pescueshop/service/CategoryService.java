package com.project.pescueshop.service;

import com.project.pescueshop.model.entity.Category;
import com.project.pescueshop.model.entity.SubCategory;
import com.project.pescueshop.repository.inteface.CategoryRepository;
import com.project.pescueshop.repository.inteface.SubCategoryRepository;
import com.project.pescueshop.util.constant.EnumStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService extends BaseService {
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    //<editor-fold desc="Category">
    @Transactional(rollbackOn = Exception.class)
    public Category addCategory(Category newCategory){
        newCategory.setStatus(EnumStatus.ACTIVE.getValue());
        Category result = categoryRepository.save(newCategory);
        return result;
    }

    public List<Category> findAllCategory(){
        return categoryRepository.findAll();
    }
    //</editor-fold>

    //<editor-fold desc="SubCategory">
    @Transactional(rollbackOn = Exception.class)
    public SubCategory addSubCategory(SubCategory newSubCategory){
        newSubCategory.setStatus(EnumStatus.ACTIVE.getValue());
        SubCategory result = subCategoryRepository.save(newSubCategory);
        return result;
    }

    public List<SubCategory> findAllSubCategory(){
        return subCategoryRepository.findAll();
    }

    public List<SubCategory> findAllSubCategoryByCategoryId(String categoryId){
        List<SubCategory> subCategoryList = subCategoryRepository.findSubCategoriesByCategoryId(categoryId);

        return subCategoryList;
    }
    //</editor-fold>
}
