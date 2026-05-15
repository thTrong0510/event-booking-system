/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nvtt.pojo.Category;
import com.nvtt.pojo.User;
import com.nvtt.repositories.CategoryRepository;
import com.nvtt.services.CategoryService;
import com.nvtt.utils.CategoryUtils.CategoryUtils;
import com.nvtt.utils.UserUtils.UserUtils;

import java.util.List;
import java.util.Map;

/**
 *
 * @author lequa
 */
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryUtils categoryUtils;

    @Autowired
    private UserUtils userUtils;
    
    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.getCategoryByName(name);
    }

    @Override
    public Category addCategory(Map<String, String> params) {
        User current = userUtils.getCurrentUser();
        if (current.getRole().getName() == "ADMIN"){
            Category cate = categoryUtils.convertParamsToCategoryObject(params);
            return categoryRepository.addCategory(cate);
        } else {
            throw new RuntimeException("You don't have permission to add Category");
        }
        
    }

    @Override
    public void deleteCategory(Category category) {
        categoryRepository.deleteCategory(category);
    }
    
    @Override
    public List<Category> getCategory(Map<String, String> params){
        return categoryRepository.getCategory(params);
    }
}
