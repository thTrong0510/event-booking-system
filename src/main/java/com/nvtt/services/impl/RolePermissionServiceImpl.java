/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services.impl;

import com.nvtt.pojo.Permission;
import com.nvtt.pojo.Role;
import com.nvtt.repositories.PermissionRepository;
import com.nvtt.repositories.RoleRepository;
import com.nvtt.services.RolePermissionService;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author vthan
 */
@Service
@Transactional
public class RolePermissionServiceImpl implements RolePermissionService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepository.getRoleById(id);
    }

    @Override
    public boolean createRole(Role role) {
        if (isRoleNameExists(role.getName())) {
            return false;
        }
        role.setIsActive(true);
        roleRepository.save(role);
        return true;
    }

    @Override
    public boolean isRoleNameExists(String name) {
        return roleRepository.existsByName(name);
    }

    @Override
    public Map<String, List<Permission>> getPermissionsGroupedByModule() {
        List<Permission> allPermissions = permissionRepository.findAll();
        // Nhóm các bản ghi có cùng thuộc tính module vào một Map Key-Value
        return allPermissions.stream().collect(Collectors.groupingBy(Permission::getModule));
    }

    @Override
    public List<Long> getPermissionIdsByRole(Long roleId) {
        List<Permission> permissions = permissionRepository.findByRoleId(roleId);
        return permissions.stream().map(Permission::getId).collect(Collectors.toList());
    }

    @Override
    public void assignPermissionsToRole(Long roleId, List<Long> permissionIds) {
        Role role = roleRepository.getRoleById(roleId);
        if (role != null) {
            Set<Permission> newPermissions = new HashSet<>();
            if (permissionIds != null && !permissionIds.isEmpty()) {
                for (Long pId : permissionIds) {
                    // Hibernate Session tự động tracking trạng thái Entity khi cập nhật
                    newPermissions.add(new Permission(pId));
                }
            }
            // Gán tập hợp quyền mới vào bảng liên kết trung gian (role_permission)
            role.setPermissions(newPermissions);
            roleRepository.save(role);
        }
    }

    @Override
    public boolean createPermission(Permission permission) {
        if (isPermissionDuplicate(permission.getApiPath(), permission.getApiMethod())) {
            return false;
        }
        // Chuẩn hóa chữ hoa cho Method và Module nhóm trước khi lưu
        permission.setApiMethod(permission.getApiMethod().toUpperCase().trim());
        permission.setModule(permission.getModule().toUpperCase().trim());

        permissionRepository.save(permission);
        return true;
    }

    @Override
    public boolean isPermissionDuplicate(String apiPath, String apiMethod) {
        return permissionRepository.exists(apiPath, apiMethod);
    }
}
