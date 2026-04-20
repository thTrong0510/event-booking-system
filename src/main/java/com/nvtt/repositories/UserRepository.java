/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.repositories;

import com.nvtt.pojo.User;

/**
 *
 * @author vthan
 */
public interface UserRepository {
    public User getUserByEmail(String email);
    public void addUser(User user);
    boolean authenticate(String email, String password);
}
