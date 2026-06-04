package com.nvtt.services;

import com.nvtt.pojo.Role;
import java.util.List;

public interface RoleService {

    Role getRoleById(Long id);

    Role getRoleByName(String name);

    List<Role> findAll();
}
