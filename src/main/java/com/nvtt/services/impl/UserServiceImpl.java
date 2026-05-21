/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.nvtt.pojo.Role;
import com.nvtt.pojo.User;
import com.nvtt.pojo.dtos.admin.RegisterRequestDTO;
import com.nvtt.pojo.dtos.user.ResUserInfoDTO;
import com.nvtt.repositories.RoleRepository;
import com.nvtt.repositories.UserRepository;
import com.nvtt.services.UserService;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
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
@PropertySource("classpath:configs.properties")
public class UserServiceImpl implements UserService {

    @Autowired
    private Environment env;

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
            System.err.println("error: upload avatar to cloudinary");
        }

        this.userRepository.addUser(user);

        // Tạo response user 
        ResUserInfoDTO userInfo = new ResUserInfoDTO(user.getId(), user.getEmail(), user.getFullName(), user.getAvatarUrl(), user.getRole().getId(), user.getRole().getName(), user.getIsActive());

        return userInfo;
    }

    @Override
    public boolean authenticate(String email, String password) {
        return this.userRepository.authenticate(email, password);
    }

    public boolean checkExistEmail(String email) {
        return this.userRepository.checkExistEmail(email);
    }

    public void addUser(RegisterRequestDTO dto) {
        User user = new User();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPassword(this.passwordEncoder.encode(dto.getPassword()));

        Role role = this.roleRepository.getRoleById(dto.getRoleId());

        user.setRole(role);

        try {
            Map res = this.cloudinary.uploader().upload(dto.getAvatarUrl().getBytes(), ObjectUtils.asMap("resource type", "auto"));
            user.setAvatarUrl(res.get("secure_url").toString());
        } catch (Exception ex) {
            System.err.print("error: upload avatar to cloudinary");
        }

        this.userRepository.addUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getUsersData(String search, Long roleId, int page) {
        int size = this.env.getProperty("pagination.page_size", Integer.class);

        int offset = (page - 1) * size;

        List<User> list = userRepository.findAll(search, roleId, offset, size);
        long totalElements = userRepository.countAll(search, roleId);
        int totalPages = (int) Math.ceil((double) totalElements / size);

        List<ResUserInfoDTO> dtos = list.stream().map(u -> new ResUserInfoDTO(
                u.getId(),
                u.getFullName(),
                u.getEmail(),
                u.getAvatarUrl(),
                u.getRole().getId(),
                u.getRole().getName(),
                u.getIsActive()
        )).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("users", dtos);
        result.put("currentPage", page);
        result.put("totalPages", totalPages);
        result.put("totalElements", totalElements);
        return result;
    }

    @Override
    public void toggleStatus(Long id) {
        userRepository.toggleStatus(id);
    }

    @Override
    public void updateUserRole(Long userId, Long roleId) {
        userRepository.updateRole(userId, roleId);
    }

    @Override
    public List<User> findByRoleName(String roleName) {
        return this.userRepository.findByRoleName(roleName);
    }

}
