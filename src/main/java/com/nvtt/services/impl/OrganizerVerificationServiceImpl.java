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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author vthan
 */
@Service
@PropertySource("classpath:configs.properties")
@Transactional
public class OrganizerVerificationServiceImpl implements OrganizerVerificationService {

    @Autowired
    private Environment env;

    @Autowired
    private OrganizerVerificationRepository verificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getVerificationsData(String status, String search, int page) {
        int size = this.env.getProperty("pagination.page_size", Integer.class);
        int offset = (page - 1) * size;

        List<OrganizerVerification> list = verificationRepository.findAll(status, search, offset, size);
        long totalElements = verificationRepository.countAll(status, search);
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
        OrganizerVerification ov = verificationRepository.findById(id);
        if (ov == null) {
            return;
        }

        User admin = userRepository.getUserByEmail(adminEmail);
        if (admin == null) {
            return;
        }

        if ("APPROVE".equalsIgnoreCase(action)) {
            // 1. Cập nhật trạng thái duyệt hồ sơ nhà tổ chức
            verificationRepository.updateStatus(id, "APPROVED", admin.getId());

            // 2. ĐỒNG BỘ: Gọi thông qua hàm nạp hạ tầng của UserRepository, không viết HQL tại đây
            userRepository.updateRole(ov.getUser().getId(), 2L); // 2L đại diện cho Role ORGANIZER

        } else if ("REJECT".equalsIgnoreCase(action)) {
            verificationRepository.updateStatus(id, "REJECTED", admin.getId());
        }
    }
}
