/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.controllers.admin;

import com.nvtt.services.OrganizerVerificationService;
import java.security.Principal;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author vthan
 */
@Controller
@RequestMapping("/admin/verifications")
public class OrganizerVerificationController {

    @Autowired
    private OrganizerVerificationService verificationService;

    @GetMapping
    public String listVerifications(
            @RequestParam(value = "status", defaultValue = "PENDING") String status,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model) {

        Map<String, Object> data = verificationService.getVerificationsData(status, search, page);
        model.addAttribute("verifications", data.get("verifications"));
        model.addAttribute("currentPage", data.get("currentPage"));
        model.addAttribute("totalPages", data.get("totalPages"));
        model.addAttribute("totalElements", data.get("totalElements"));
        model.addAttribute("status", status);
        model.addAttribute("search", search);
        model.addAttribute("activePage", "verifications"); // Kích hoạt sáng menu sidebar

        return "admin/verifications/list";
    }

    @PostMapping("/{id}/action")
    public String handleAction(@PathVariable("id") Long id,
            @RequestParam("action") String action,
            @RequestParam(value = "status", defaultValue = "PENDING") String status,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "1") int page,
            Principal principal) {

        // principal.getName() trả về Username/Email của Admin đang login phối hợp thực hiện lệnh
        verificationService.processVerification(id, action, principal.getName());

        return "redirect:/admin/verifications?status=" + status
                + "&search=" + (search != null ? search : "")
                + "&page=" + page;
    }
}
