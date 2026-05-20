/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services;

import java.util.Map;

/**
 *
 * @author vthan
 */
public interface OrganizerVerificationService {

    Map<String, Object> getVerificationsData(String status, String search, int page);

    void processVerification(Long id, String action, String adminEmail);
}
