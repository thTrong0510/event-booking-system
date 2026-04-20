/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services;

import com.nvtt.pojo.User;
import com.nvtt.pojo.dtos.user.ResUserInfo;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author vthan
 */
public interface UserService {
    public User getUserByEmail(String email);
    public ResUserInfo addUser(Map<String, String> params, MultipartFile avatar);
}
