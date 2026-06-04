package com.nvtt.services;

import java.util.List;
import java.util.Map;

import com.nvtt.pojo.OrganizerVerification;
import com.nvtt.utils.exceptions.IdInvalidException;

public interface OrganizerVerificationService {

    OrganizerVerification addOrganizerVerification(Map<String, String> params) throws IdInvalidException;

    List<OrganizerVerification> getOrganizerVerifications(Map<String, String> params);

    OrganizerVerification getOrganizerVerificationById(Long id);

    Map<String, Object> getVerificationsData(String status, String search, int page);

    void processVerification(Long id, String action, String adminEmail);
}
