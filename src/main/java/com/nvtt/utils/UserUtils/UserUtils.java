package com.nvtt.utils.UserUtils;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.nvtt.pojo.User;
import com.nvtt.pojo.dtos.user.ResUserInfoDTO;
import com.nvtt.services.UserService;

@Component
public class UserUtils {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public ResUserInfoDTO convertToResUserInfoDTO(User user) {
        try {
            ResUserInfoDTO dto = new ResUserInfoDTO();
            dto.setId(user.getId());
            dto.setEmail(user.getEmail());
            dto.setFullName(user.getFullName());
            dto.setAvatarUrl(user.getAvatarUrl());
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert User to Response User");
        }
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String email = authentication.getName();
            return userService.getUserByEmail(email);
        } else {
            throw new RuntimeException("Can't authenticate curren user");
        }
    }

    public User convertParamsToUser(Map<String, String> params) {
        try {
            User user = new User();
            if (params.containsKey("fullName")) {
                user.setFullName(params.get("fullName"));
            }
            if (params.containsKey("email")) {
                user.setEmail(params.get("email"));
            }
            if (params.containsKey("password")) {
                user.setPassword(this.passwordEncoder.encode(params.get("password")));
            }
            return user;
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert params to user");
        }

    }

    public User converParamsToUserForUpdating(User user, Map<String, String> params) {
        try {
            if (params.containsKey("fullName")) {
                user.setFullName(params.get("fullName"));
            }
            if (params.containsKey("email")) {
                user.setEmail(params.get("email"));
            }
            if (params.containsKey("password")) {
                user.setPassword(this.passwordEncoder.encode(params.get("password")));
            }
            return user;
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert params to user");
        }
    }
}
