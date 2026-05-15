/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nvtt.pojo.OrganizerVerification;
import com.nvtt.pojo.User;
import com.nvtt.repositories.OrganizerVerificationRepository;
import com.nvtt.services.OrganizerVerificationService;
import com.nvtt.utils.UserUtils.UserUtils;
import com.nvtt.utils.constants.OrganizerVerificationStatus;

/**
 *
 * @author lequa
 */
@Service
@Transactional
public class OrganizerVerificationServiceImpl implements OrganizerVerificationService {

    @Autowired
    private OrganizerVerificationRepository organizerVerificationRepository;
    
    @Autowired
    private UserUtils userUtils;

    @Override
    public OrganizerVerification addOrganizerVerification(Map<String, String> params) {
        try {
            User currentUser = userUtils.getCurrentUser();
            if (currentUser == null) {
                throw new RuntimeException("User not authenticated");
            }
            if (currentUser.getOrganizerVerification() != null) {
                throw new RuntimeException("User has already applied for organizer verification");
            }
            if (currentUser.getRole().getName().equals("ORGANIZER")) {
                throw new RuntimeException("User is already an organizer");
            }
            OrganizerVerification organizerVerification = new OrganizerVerification();
            organizerVerification.setUser(currentUser);
            OrganizerVerificationStatus status = OrganizerVerificationStatus.valueOf("PENDING");
            organizerVerification.setStatus(status);
            return organizerVerificationRepository.addOrganizerVerification(organizerVerification);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<OrganizerVerification> getOrganizerVerifications(Map<String, String> params){
        return organizerVerificationRepository.getOrganizerVerifications(params);
    }

    @Override
    public OrganizerVerification getOrganizerVerificationById(Long id){
        return organizerVerificationRepository.getOrganizerVerificationById(id);
    }
}
