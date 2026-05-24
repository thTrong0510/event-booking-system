/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services;

import com.nvtt.pojo.Role;

/**
 *
 * @author vthan
 */
public interface RoleService {
    Role getRoleById(Long id);
    Role getRoleByName(String name);
}
