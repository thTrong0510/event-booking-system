/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services;

import com.nvtt.pojo.Category;
import com.nvtt.pojo.dtos.admin.CategoryDTO;
import java.util.List;
import java.util.Map;

/**
 *
 * @author lequa
 */
public interface CategoryService {

    Category getCategoryByName(String name);

    Category addCategory(Map<String, String> params);

    Category updateCategory(Long id, Map<String, String> params);

    void deleteCategory(Category category);

    List<Category> getCategory(Map<String, String> params);

    List<CategoryDTO> getAllCategories();

    CategoryDTO getCategoryById(Long id);
}
