/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.controllers.admin;

import com.nvtt.pojo.User;
import com.nvtt.services.RoleService;
import com.nvtt.services.UserService;
import com.nvtt.services.email.EmailService;
import com.nvtt.utils.constants.EmailType;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author vthan
 */
@Controller
@RequestMapping("/admin/users")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private EmailService emailService;

    @GetMapping
    public String listUsers(@RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "roleId", required = false) Long roleId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model) {
        logger.info("start sql listUsers admin");
        Map<String, Object> data = userService.getUsersData(search, roleId, page);

        model.addAllAttributes(data);
        model.addAttribute("search", search);
        model.addAttribute("roleId", roleId);
        model.addAttribute("allRoles", roleService.findAll());
        logger.info("end sql");
        return "admin/users/list";
    }

    @PostMapping("/{id}/toggle-status")
    public String toggleStatus(@PathVariable("id") Long id,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "roleId", required = false) Long roleId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            RedirectAttributes redirectAttributes) {
        logger.info("start sql toggleStatus user admin");
        User user = userService.toggleStatus(id);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật trạng thái tài khoản thành công!");

        EmailType emailType = user.getIsActive() ? EmailType.ACCOUNT_UNLOCKED : EmailType.ACCOUNT_LOCKED;

        emailService.sendAccountNotification(
                user.getEmail(),
                user.getFullName(),
                emailType
        );

        logger.info("end sql");

        return "redirect:/admin/users?search=" + (search != null ? search : "")
                + "&roleId=" + (roleId != null ? roleId : "")
                + "&page=" + page;
    }

    @PostMapping("/{id}/update-role")
    public String updateRole(@PathVariable("id") Long id,
            @RequestParam("newRoleId") Long newRoleId,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "roleId", required = false) Long roleId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            RedirectAttributes redirectAttributes) {
        logger.info("start sql updateRole");
        userService.updateUserRole(id, newRoleId);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật vai trò hệ thống thành công!");
        logger.info("end sql");
        return "redirect:/admin/users?search=" + (search != null ? search : "")
                + "&roleId=" + (roleId != null ? roleId : "")
                + "&page=" + page;
    }
}
