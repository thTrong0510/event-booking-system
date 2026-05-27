/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services;

import com.nvtt.pojo.User;
import com.nvtt.pojo.dtos.admin.RegisterRequestDTO;
import com.nvtt.pojo.dtos.user.ResUserInfoDTO;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author vthan
 */
public interface UserService {

    public User getUserByEmail(String email);
    
    public User getUserById(Long id);

    public ResUserInfoDTO addUser(Map<String, String> params, MultipartFile avatar);

    boolean authenticate(String email, String password);
    
    public User getMyInfo();

    public ResUserInfoDTO updateMyInfo(Map<String, String> params, Optional<MultipartFile> avatar);

    boolean checkExistEmail(String email);

    public void addUser(RegisterRequestDTO dto);

    Map<String, Object> getUsersData(String search, Long roleId, int page);

    User toggleStatus(Long id);

    void updateUserRole(Long userId, Long roleId);
    
    List<User> findByRoleName(String roleName);
    
    boolean checkActiveAccount(String email);
}
