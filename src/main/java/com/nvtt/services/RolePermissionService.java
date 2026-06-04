package com.nvtt.services;

import com.nvtt.pojo.Permission;
import com.nvtt.pojo.Role;
import java.util.List;
import java.util.Map;

public interface RolePermissionService {

    List<Role> getAllRoles();

    Role getRoleById(Long id);

    boolean createRole(Role role);

    boolean isRoleNameExists(String name);

    Map<String, List<Permission>> getPermissionsGroupedByModule();

    List<Long> getPermissionIdsByRole(Long roleId);

    void assignPermissionsToRole(Long roleId, List<Long> permissionIds);

    boolean createPermission(Permission permission);

    boolean isPermissionDuplicate(String apiPath, String apiMethod);
}
