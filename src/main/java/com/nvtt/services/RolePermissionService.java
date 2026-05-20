/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services;

import com.nvtt.pojo.Permission;
import com.nvtt.pojo.Role;
import java.util.List;
import java.util.Map;

/**
 *
 * @author vthan
 */
public interface RolePermissionService {

    List<Role> getAllRoles();

    Role getRoleById(Long id);

    boolean createRole(Role role);

    boolean isRoleNameExists(String name);

    // Gom nhóm Permission theo thuộc tính module để FE hiển thị thành cụm
    Map<String, List<Permission>> getPermissionsGroupedByModule();

    List<Long> getPermissionIdsByRole(Long roleId);

    void assignPermissionsToRole(Long roleId, List<Long> permissionIds);

    boolean createPermission(Permission permission);

    boolean isPermissionDuplicate(String apiPath, String apiMethod);
}
