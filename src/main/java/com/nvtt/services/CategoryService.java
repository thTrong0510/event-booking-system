/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services;

import com.nvtt.pojo.Category;
import java.util.List;
import java.util.Map;

/**
 *
 * @author lequa
 */
public interface CategoryService {
    Category getCategoryByName(String name);
    Category addCategory(Map<String, String> params); // add and modify
    void deleteCategory(Category category);
    List<Category> getCategory(Map<String, String> params);
}
