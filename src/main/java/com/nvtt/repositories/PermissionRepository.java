/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.repositories;

import com.nvtt.pojo.Permission;
import java.util.List;

/**
 *
 * @author vthan
 */
public interface PermissionRepository {

    Permission save(Permission permission);

    void delete(Long id);

    Permission findById(Long id);

    List<Permission> findAll(String search,
            String module,
            String apiMethod,
            int offset,
            int limit);

    long countAll(String search,
            String module,
            String apiMethod);

    boolean exists(String name, String apiPath, String apiMethod);
}
