/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services.email;

import com.nvtt.utils.constants.EmailType;

/**
 *
 * @author vthan
 */
public interface EmailService {

    void sendAccountNotification(
            String to,
            String name,
            EmailType type
    );
}
