package com.nvtt.controllers.apis.organizer_application;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nvtt.pojo.OrganizerVerification;
import com.nvtt.pojo.dtos.organizer_verification.ResOrganizerApplicationDTO;
import com.nvtt.services.OrganizerVerificationService;
import com.nvtt.utils.exceptions.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class ApiOrganizerApplicationController {

    private static final Logger logger = LogManager.getLogger(ApiOrganizerApplicationController.class);

    @Autowired
    private OrganizerVerificationService organizerVerificationService;

    @PostMapping("/me/organizer-application")
    public ResponseEntity<ResOrganizerApplicationDTO> addOrganizerApplication(@RequestParam Map<String, String> params) throws IdInvalidException {
        logger.info("start sql addOrganizerApplication");
        OrganizerVerification addedVerification = organizerVerificationService.addOrganizerVerification(params);
        ResOrganizerApplicationDTO dto = new ResOrganizerApplicationDTO();
        dto.setUserId(addedVerification.getUser().getId());
        dto.setStatus(addedVerification.getStatus());
        logger.info("end sql");
        return ResponseEntity.ok(dto);
    }
}
