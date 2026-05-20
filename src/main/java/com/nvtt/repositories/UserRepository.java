/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.repositories;

import com.nvtt.pojo.User;
import java.util.List;

/**
 *
 * @author vthan
 */
public interface UserRepository {
    public User getUserByEmail(String email);
    public void addUser(User user);
    boolean authenticate(String email, String password);
    List<User> findAll(String search, Long roleId, int offset, int limit);
    long countAll(String search, Long roleId);
    User getUserById(Long id);
    void updateStatus(Long id, boolean isActive);
    void updateRole(Long userId, Long roleId);
    boolean checkExistEmail(String email);
}
