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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    
    private static final Logger logger = LogManager.getLogger(ApiCategoryController.class);
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private CategoryUtils categoryUtils;
    
    @GetMapping("/categories")
    public ResponseEntity<List<ResCategoryInfoDTO>> getCategories(@RequestParam Map<String, String> params) {
        logger.info("start sql getCategories");
        List<Category> categories = categoryService.getCategory(params);
        List<ResCategoryInfoDTO> dto = categoryUtils.convertToResCategoryInfoDTOList(categories);
        logger.info("end sql");
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PostMapping("secure/categories")
    public ResponseEntity<ResCategoryInfoDTO> addCategory(@RequestParam Map<String, String> params) {
        logger.info("start sql addCategory");
        Category category = categoryService.addCategory(params);
        ResCategoryInfoDTO dto = categoryUtils.convertToResCategoryInfoDTO(category);
        logger.info("end sql");
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
}
