/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services.impl;

import com.nvtt.pojo.Category;
import com.nvtt.pojo.dtos.admin.CategoryDTO;
import com.nvtt.repositories.CategoryRepository;
import com.nvtt.services.CategoryService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author vthan
 */
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        
        // Ánh xạ an toàn từ POJO Entity sang DTO
        return categories.stream().map(cat -> new CategoryDTO(
            cat.getId(),
            cat.getName(),
            cat.getDescription()
        )).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO getCategoryById(Long id) {
        Category cat = categoryRepository.findById(id);
        if (cat == null) {
            return null;
        }
        return new CategoryDTO(cat.getId(), cat.getName(), cat.getDescription());
    }
}