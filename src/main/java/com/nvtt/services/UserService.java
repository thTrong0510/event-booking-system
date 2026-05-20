/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services;

import com.nvtt.pojo.User;
import com.nvtt.pojo.dtos.admin.RegisterRequestDTO;
import com.nvtt.pojo.dtos.user.ResUserInfoDTO;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author vthan
 */
public interface UserService {

    public User getUserByEmail(String email);

    public ResUserInfoDTO addUser(Map<String, String> params, MultipartFile avatar);

    boolean authenticate(String email, String password);
    
    boolean checkExistEmail(String email);
    
    public void addUser(RegisterRequestDTO dto);
}
