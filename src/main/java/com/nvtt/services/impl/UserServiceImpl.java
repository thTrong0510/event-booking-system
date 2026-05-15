/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.nvtt.pojo.Role;
import com.nvtt.pojo.User;
import com.nvtt.pojo.dtos.user.ResUserInfoDTO;
import com.nvtt.repositories.RoleRepository;
import com.nvtt.repositories.UserRepository;
import com.nvtt.services.UserService;
import java.io.IOException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author vthan
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public User getUserByEmail(String email) {
        return this.userRepository.getUserByEmail(email);
    }

    @Override
    public ResUserInfoDTO addUser(Map<String, String> params, MultipartFile avatar) {
        User user = new User();
        user.setFullName(params.get("fullName"));
        user.setEmail(params.get("email"));
        user.setPassword(this.passwordEncoder.encode(params.get("password")));
        
        Role role = this.roleRepository.getRoleById(Long.valueOf(params.get("role")));
        user.setRole(role);

        // role chỉ cần truyền lên id -> quét 
        
        try {
            // resource type: auto -> là để cloudinary tự ép kiểu cho từng loại file gửi lên
            Map res = this.cloudinary.uploader().upload(avatar.getBytes(), ObjectUtils.asMap("resource type", "auto"));
            user.setAvatarUrl(res.get("secure_url").toString());

            // tới đây cần parser -> cũng cần phải khai báo Bean tương tự 
        } catch (IOException ex) {
            System.err.println("errorr");
        }

        this.userRepository.addUser(user);

        // Tạo response user 
        ResUserInfoDTO userInfo = new ResUserInfoDTO(user.getId(), user.getEmail(), user.getFullName(), user.getAvatarUrl());

        return userInfo;
    }

    @Override
    public boolean authenticate(String email, String password) {
        return this.userRepository.authenticate(email, password);
    }
    
    @Override
    public User getMyInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String email = authentication.getName();
            User user = userRepository.getUserByEmail(email);
            return user;
        }
        return null;
    }

}
