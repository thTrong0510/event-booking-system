
package com.nvtt.utils.CategoryUtils;

import com.nvtt.pojo.Category;
import com.nvtt.pojo.dtos.category.ResCategoryInfoDTO;
import com.nvtt.pojo.dtos.event.ResEventMediaDTO;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;


@Component
public class CategoryUtils {

    public List<ResCategoryInfoDTO> convertToResCategoryInfoDTOList(List<Category> categories) {
        return categories.stream()
                .map(category -> new ResCategoryInfoDTO(
                category.getId(),
                category.getName()
        )).collect(Collectors.toList());
    }

    public ResCategoryInfoDTO convertToResCategoryInfoDTO(Category category){
        ResCategoryInfoDTO cate = new ResCategoryInfoDTO(category.getId(), category.getName());
        return cate;
    }

    public Category convertParamsToCategoryObject(Map<String, String> params){
        try {
            Category cate = new Category();
            cate.setDescription(params.get("description"));
            cate.setName(params.get("name"));
            return cate;
        } catch (Exception e) {
            throw new RuntimeException("Error in params", e);
        }
    }
}
