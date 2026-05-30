/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.controllers.admin;

import com.nvtt.pojo.Role;
import com.nvtt.pojo.dtos.admin.RegisterRequestDTO;
import com.nvtt.repositories.RoleRepository;
import com.nvtt.services.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author vthan
 */
@Controller
@RequestMapping("/admin")
public class AuthController {
    
    private static final Logger logger = LogManager.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @ModelAttribute("roles")
    public List<Role> populateRoles() {
        logger.info("start sql modelAttribute populateRoles admin/AuthenController");
        List<Role> roles = roleRepository.findAll();
        logger.info("end sql");
        return roles;
    }
    
    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Email hoặc mật khẩu không chính xác!");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "Bạn đã đăng xuất thành công.");
        }
        return "admin/auth/login";
    }
    
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("registerDTO", new RegisterRequestDTO());
        return "admin/auth/register";
    }
    
    @PostMapping("/register")
    public String registerAdmin(@ModelAttribute("registerDTO") @Valid RegisterRequestDTO dto,
            BindingResult registerResult,
            RedirectAttributes redirectAttributes) {
            
        if (registerResult.hasErrors()) {
            return "admin/auth/register";
        }

        try {
            logger.info("start sql register");
            userService.addUser(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Đăng ký tài khoản Admin thành công! Hãy đăng nhập.");
            logger.info("end sql");
            return "redirect:/admin/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đăng ký thất bại: " + e.getMessage());
            return "redirect:/admin/register";
        }
    }
}
