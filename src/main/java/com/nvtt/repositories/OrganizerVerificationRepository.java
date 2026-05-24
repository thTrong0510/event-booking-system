/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.repositories;

import java.util.List;
import java.util.Map;

import com.nvtt.pojo.OrganizerVerification;

/**
 *
 * @author lequa
 */
public interface OrganizerVerificationRepository {
    OrganizerVerification addOrganizerVerification(OrganizerVerification organizerVerification);
    List<OrganizerVerification> getOrganizerVerifications(Map<String, String> params);
    OrganizerVerification getOrganizerVerificationById(Long id);
}
