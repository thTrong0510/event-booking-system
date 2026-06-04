package com.nvtt.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/errors")
public class ErrorPageController {

    @GetMapping("/404")
    public String handle404(Model model) {
        model.addAttribute("activePage", "error");
        return "admin/errors/404";
    }

    @GetMapping("/401")
    public String handle401Or403(Model model) {
        model.addAttribute("activePage", "error");
        return "admin/errors/401";
    }
}
