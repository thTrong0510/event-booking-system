package com.nvtt.repositories;

import com.nvtt.pojo.User;
import java.util.List;

public interface UserRepository {

    public User getUserByEmail(String email);

    public void addUser(User user);

    boolean authenticate(String email, String password);

    List<User> findAll(String search, Long roleId, int offset, int limit);

    long countAll(String search, Long roleId);

    User getUserById(Long id);

    User toggleStatus(Long id);

    void updateRole(Long userId, Long roleId);

    boolean checkExistEmail(String email);

    List<User> findByRoleName(String roleName);

    boolean checkActiveAccount(String email);
}
