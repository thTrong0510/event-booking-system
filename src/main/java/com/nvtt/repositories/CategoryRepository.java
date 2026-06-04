package com.nvtt.repositories;

import com.nvtt.pojo.Category;
import java.util.List;
import java.util.Map;

public interface CategoryRepository {

    Category getCategoryByName(String name);

    Category addCategory(Category category);

    void deleteCategory(Category category);

    List<Category> getCategory(Map<String, String> params);

    Category getCategoryById(Long id);

    List<Category> findAll();

    Category findById(Long id);
}
