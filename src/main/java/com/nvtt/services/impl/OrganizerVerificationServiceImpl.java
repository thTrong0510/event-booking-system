/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services.impl;

import com.nvtt.pojo.OrganizerVerification;
import com.nvtt.pojo.User;
import com.nvtt.pojo.dtos.response.OrganizerVerificationResponseDTO;
import com.nvtt.repositories.OrganizerVerificationRepository;
import com.nvtt.repositories.UserRepository;
import com.nvtt.services.OrganizerVerificationService;
import com.nvtt.utils.DateTimeUtil;
import com.nvtt.utils.UserUtils.UserUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nvtt.utils.constants.OrganizerVerificationStatus;

/**
 *
 * @author vthan
 */
@Service
@PropertySource("classpath:configs.properties")
@Transactional
public class OrganizerVerificationServiceImpl implements OrganizerVerificationService {

    @Autowired
    private OrganizerVerificationRepository organizerVerificationRepository;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private Environment env;

    @Autowired
    private UserRepository userRepository;

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

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getVerificationsData(String status, String search, int page) {
        int size = this.env.getProperty("pagination.page_size", Integer.class);
        int offset = (page - 1) * size;

        List<OrganizerVerification> list = organizerVerificationRepository.findAll(status, search, offset, size);
        long totalElements = organizerVerificationRepository.countAll(status, search);
        int totalPages = (int) Math.ceil((double) totalElements / size);

        List<OrganizerVerificationResponseDTO> dtos = list.stream().map(ov -> new OrganizerVerificationResponseDTO(
                ov.getId(),
                ov.getUser().getId(),
                ov.getUser().getFullName(),
                ov.getUser().getEmail(),
                ov.getUser().getAvatarUrl(),
                ov.getStatus(),
                ov.getApprovedBy() != null ? ov.getApprovedBy().getFullName() : "N/A",
                DateTimeUtil.dateToString(ov.getApprovedAt()),
                DateTimeUtil.dateToString(ov.getCreatedAt())
        )).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("verifications", dtos);
        result.put("currentPage", page);
        result.put("totalPages", totalPages);
        result.put("totalElements", totalElements);
        return result;
    }

    @Override
    public void processVerification(Long id, String action, String adminEmail) {
        OrganizerVerification ov = organizerVerificationRepository.findById(id);
        if (ov == null) {
            return;
        }

        User admin = userRepository.getUserByEmail(adminEmail);
        if (admin == null) {
            return;
        }

        if ("APPROVE".equalsIgnoreCase(action)) {
            organizerVerificationRepository.updateStatus(id, "APPROVED", admin.getId());

            userRepository.updateRole(ov.getUser().getId(), 2L);

        } else if ("REJECT".equalsIgnoreCase(action)) {
            organizerVerificationRepository.updateStatus(id, "REJECTED", admin.getId());
        }
    }
}
