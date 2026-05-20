/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.controllers.admin;

import com.nvtt.services.RoleService;
import com.nvtt.services.UserService;
import java.util.Map;
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
public class AdminUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping
    public String listUsers(@RequestParam(value = "search", required = false) String search,
                            @RequestParam(value = "roleId", required = false) Long roleId,
                            @RequestParam(value = "page", defaultValue = "1") int page,
                            Model model) {
        
        Map<String, Object> data = userService.getUsersData(search, roleId, page);
        
        model.addAllAttributes(data);
        model.addAttribute("search", search);
        model.addAttribute("roleId", roleId);
        model.addAttribute("allRoles", roleService.findAll()); // Dành cho dropdown lọc và dropdown sửa quyền
        model.addAttribute("activePage", "users");
        
        return "admin/users/list";
    }

    // Chặn / Kích hoạt tài khoản người dùng
    @PostMapping("/{id}/toggle-status")
    public String toggleStatus(@PathVariable("id") Long id,
                               @RequestParam(value = "search", required = false) String search,
                               @RequestParam(value = "roleId", required = false) Long roleId,
                               @RequestParam(value = "page", defaultValue = "1") int page,
                               RedirectAttributes redirectAttributes) {
        userService.toggleStatus(id);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật trạng thái tài khoản thành công!");
        
        // Giữ nguyên trạng thái phân trang và bộ lọc sau khi thực thi xong hành động
        return "redirect:/admin/users?search=" + (search != null ? search : "") 
                + "&roleId=" + (roleId != null ? roleId : "") 
                + "&page=" + page;
    }

    // Cập nhật quyền (Role) cho người dùng
    @PostMapping("/{id}/update-role")
    public String updateRole(@PathVariable("id") Long id,
                             @RequestParam("newRoleId") Long newRoleId,
                             @RequestParam(value = "search", required = false) String search,
                             @RequestParam(value = "roleId", required = false) Long roleId,
                             @RequestParam(value = "page", defaultValue = "1") int page,
                             RedirectAttributes redirectAttributes) {
        userService.updateUserRole(id, newRoleId);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật vai trò hệ thống thành công!");
        
        return "redirect:/admin/users?search=" + (search != null ? search : "") 
                + "&roleId=" + (roleId != null ? roleId : "") 
                + "&page=" + page;
    }
}
