/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.controllers.apis.category;

import com.nvtt.pojo.Category;
import com.nvtt.pojo.dtos.category.ResCategoryInfoDTO;
import com.nvtt.services.CategoryService;
import com.nvtt.utils.CategoryUtils.CategoryUtils;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lequa
 */
@RestController
@RequestMapping("/api")
public class ApiCategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private CategoryUtils categoryUtils;
    
    @GetMapping("/categories")
    public ResponseEntity<List<ResCategoryInfoDTO>> getEvents(@RequestParam Map<String, String> params) {
        try {
            List<Category> categories = categoryService.getCategory(params);
            List<ResCategoryInfoDTO> dto = categoryUtils.convertToResCategoryInfoDTOList(categories);
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        } catch (Exception e) {
            System.err.println("Error fetching all categories: " + e.getMessage());
            throw new RuntimeException("Error fetching all categories", e);
        }
    }

    @PostMapping("secure/categories")
    public ResponseEntity<ResCategoryInfoDTO> addEvents(@RequestParam Map<String, String> params) {
        try {
            Category category = categoryService.addCategory(params);
            ResCategoryInfoDTO dto = categoryUtils.convertToResCategoryInfoDTO(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (Exception e) {
            throw new RuntimeException("Error add category: " + e.getMessage());
        }
    }
}
