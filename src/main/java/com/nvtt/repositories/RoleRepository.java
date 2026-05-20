/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.repositories;

import com.nvtt.pojo.Role;
import java.util.List;

/**
 *
 * @author vthan
 */
public interface RoleRepository {

    Role getRoleById(Long id);

    Role getRoleByName(String name);

    Role save(Role role);

    void delete(Long id);

    boolean existsByName(String name);

    List<Role> findAll(String search,
            Boolean isActive,
            int offset,
            int limit);

    List<Role> findAll();
}
