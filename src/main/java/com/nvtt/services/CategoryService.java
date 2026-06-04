package com.nvtt.services;

import com.nvtt.pojo.Category;
import com.nvtt.pojo.dtos.admin.CategoryDTO;
import java.util.List;
import java.util.Map;

public interface CategoryService {

    Category getCategoryByName(String name);

    Category addCategory(Map<String, String> params);

    Category updateCategory(Long id, Map<String, String> params);

    void deleteCategory(Category category);

    List<Category> getCategory(Map<String, String> params);

    List<CategoryDTO> getAllCategories();

    CategoryDTO getCategoryById(Long id);
}
