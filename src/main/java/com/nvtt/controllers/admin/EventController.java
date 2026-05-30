/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.controllers.admin;

import com.nvtt.pojo.dtos.admin.EventSearchCriteriaDTO;
import com.nvtt.services.CategoryService;
import com.nvtt.services.EventService;
import com.nvtt.services.EventStatusService;
import com.nvtt.services.UserService;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
@RequestMapping("/admin/events")
public class EventController {
    
    private static final Logger logger = LogManager.getLogger(EventController.class);

    @Autowired
    private EventService adminEventService;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private EventStatusService eventStatusService;
    
    @Autowired
    private UserService userService;

    @GetMapping
    public String listEvents(@ModelAttribute("criteria") EventSearchCriteriaDTO criteria, Model model) {
        logger.info("start sql listEvents admin");
        Map<String, Object> data = adminEventService.getFilteredEvents(criteria);
        model.addAllAttributes(data);
        model.addAttribute("activePage", "events");
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("statuses", eventStatusService.getAllStatuses());
        model.addAttribute("organizers", userService.findByRoleName("ORGANIZER"));
        logger.info("end sql");
        return "admin/events/list";
    }

    @GetMapping("/{id}")
    public String viewDetails(@PathVariable("id") Long id, Model model) {
        logger.info("start sql viewDetails admin");
        model.addAttribute("event", adminEventService.getEventDetails(id));
        logger.info("end sql");
        return "admin/events/detail";
    }

    @PostMapping("/{id}/approve")
    public String approve(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        logger.info("start sql approve event admin");
        adminEventService.approveEvent(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã duyệt sự kiện hiển thị công khai thành công!");
        logger.info("end sql");
        return "redirect:/admin/events";
    }

    @PostMapping("/{id}/reject")
    public String reject(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        logger.info("start sql reject event admin");
        adminEventService.rejectEvent(id);
        redirectAttributes.addFlashAttribute("infoMessage", "Đã từ chối duyệt nội dung sự kiện.");
        logger.info("end sql");
        return "redirect:/admin/events";
    }

    @PostMapping("/{id}/violation")
    public String handleViolation(@PathVariable("id") Long id, @RequestParam("action") String action, RedirectAttributes redirectAttributes) {
        logger.info("start sql handleViolation event admin");
        if ("HIDE".equalsIgnoreCase(action)) {
            adminEventService.updateStatus(id, "HIDDEN");
            redirectAttributes.addFlashAttribute("warningMessage", "Đã ẩn sự kiện khỏi giao diện người dùng.");
        } else if ("DELETE".equalsIgnoreCase(action)) {
            adminEventService.updateStatus(id, "DELETED");
            redirectAttributes.addFlashAttribute("dangerMessage", "Đã đánh dấu xóa sự kiện do vi phạm chính sách nghiêm trọng.");
        }
        logger.info("end sql");
        return "redirect:/admin/events";
    }
}
