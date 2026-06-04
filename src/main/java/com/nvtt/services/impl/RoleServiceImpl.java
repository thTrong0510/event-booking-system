package com.nvtt.services.impl;

import com.nvtt.pojo.Role;
import com.nvtt.repositories.RoleRepository;
import com.nvtt.services.RoleService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role getRoleById(Long id) {
        return this.getRoleById(id);
    }

    @Override
    public Role getRoleByName(String name) {
        return this.getRoleByName(name);
    }

    @Override
    public List<Role> findAll() {
        return this.roleRepository.findAll();
    }

}
