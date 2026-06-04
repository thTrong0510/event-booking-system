package com.nvtt.repositories;

import com.nvtt.pojo.Role;
import java.util.List;

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
