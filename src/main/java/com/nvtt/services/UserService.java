/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services;

import com.nvtt.pojo.User;
import com.nvtt.pojo.dtos.user.ResUserInfoDTO;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author vthan
 */
public interface UserService {

    public User getUserByEmail(String email);

    public ResUserInfoDTO addUser(Map<String, String> params, MultipartFile avatar);

    boolean authenticate(String email, String password);
    
    public User getMyInfo();

    public ResUserInfoDTO updateMyInfo(Map<String, String> params, Optional<MultipartFile> avatar);
}
