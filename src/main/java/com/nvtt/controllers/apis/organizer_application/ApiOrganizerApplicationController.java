/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.controllers.apis.organizer_application;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nvtt.pojo.OrganizerVerification;
import com.nvtt.pojo.dtos.organizer_verification.ResOrganizerApplicationDTO;
import com.nvtt.services.OrganizerVerificationService;

/**
 *
 * @author lequa
 */
@RestController
@RequestMapping("/api")
public class ApiOrganizerApplicationController {

    @Autowired
    private OrganizerVerificationService organizerVerificationService;
    
    @PostMapping("secure/organizer-application")
    public ResponseEntity<ResOrganizerApplicationDTO> addOrganizerApplication(@RequestParam Map<String, String> params) {
        try {
            OrganizerVerification addedVerification = organizerVerificationService.addOrganizerVerification(params);
            ResOrganizerApplicationDTO dto = new ResOrganizerApplicationDTO();
            dto.setUserId(addedVerification.getUser().getId());
            dto.setStatus(addedVerification.getStatus());
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
