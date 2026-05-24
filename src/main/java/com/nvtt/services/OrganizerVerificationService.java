/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services;

import java.util.List;
import java.util.Map;

import com.nvtt.pojo.OrganizerVerification;

/**
 *
 * @author lequa
 */
public interface OrganizerVerificationService {
    OrganizerVerification addOrganizerVerification(Map<String, String> params);
    List<OrganizerVerification> getOrganizerVerifications(Map<String, String> params);
    OrganizerVerification getOrganizerVerificationById(Long id);
        Map<String, Object> getVerificationsData(String status, String search, int page);

    void processVerification(Long id, String action, String adminEmail);
}
