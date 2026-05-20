/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.repositories;

import com.nvtt.pojo.OrganizerVerification;
import java.util.List;

/**
 *
 * @author vthan
 */
public interface OrganizerVerificationRepository {
    List<OrganizerVerification> findAll(String status, String search, int offset, int limit);
    long countAll(String status, String search);
    OrganizerVerification findById(Long id);
    void updateStatus(Long id, String status, Long adminId);
}
