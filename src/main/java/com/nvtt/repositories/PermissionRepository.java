package com.nvtt.repositories;

import com.nvtt.pojo.Permission;
import java.util.List;

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

    boolean exists(String apiPath, String apiMethod);

    List<Permission> findByRoleId(Long roleId);

    List<Permission> findAll();
}
