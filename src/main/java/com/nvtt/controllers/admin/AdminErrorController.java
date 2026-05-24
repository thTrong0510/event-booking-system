/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author vthan
 */
@Controller
@RequestMapping("/admin/errors")
public class AdminErrorController {

    @GetMapping("/404")
    public String handle404(Model model) {
        model.addAttribute("activePage", "error"); // Tránh làm sáng các menu khác
        return "admin/errors/404";
    }

    @GetMapping("/401")
    public String handle401Or403(Model model) {
        model.addAttribute("activePage", "error");
        return "admin/errors/401";
    }
}
