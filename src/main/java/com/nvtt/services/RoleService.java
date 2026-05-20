/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services;

import com.nvtt.pojo.Role;
import java.util.List;

/**
 *
 * @author vthan
 */
public interface RoleService {
    Role getRoleById(Long id);
    List<Role> findAll();
}
