/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.controllers.admin;

import com.nvtt.pojo.Permission;
import com.nvtt.pojo.Role;
import com.nvtt.services.RolePermissionService;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
public class RolePermissionController {
    
    private static final Logger logger = LogManager.getLogger(RolePermissionController.class);

    @Autowired
    private RolePermissionService rolePermissionService;
    
    @GetMapping("/roles-permissions")
    public String showPage(@RequestParam(value = "roleId", required = false) Long roleId, Model model) {
        logger.info("start sql showPage role permission");
        model.addAttribute("roles", rolePermissionService.getAllRoles());
        model.addAttribute("permissionGrouped", rolePermissionService.getPermissionsGroupedByModule());
        
        if (!model.containsAttribute("newRole")) {
            model.addAttribute("newRole", new Role());
        }
        if (!model.containsAttribute("newPermission")) {
            model.addAttribute("newPermission", new Permission());
        }

        if (roleId != null) {
            Role currentRole = rolePermissionService.getRoleById(roleId);
            if (currentRole != null) {
                model.addAttribute("currentRole", currentRole);
                model.addAttribute("assignedPermissionIds", rolePermissionService.getPermissionIdsByRole(roleId));
            }
        }
        model.addAttribute("activePage", "roles-permissions");
        logger.info("end sql");
        return "admin/authorization/roles-permissions";
    }

    @PostMapping("/roles/create")
    public String handleCreateRole(@ModelAttribute("newRole") Role role,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        logger.info("start sql handleCreateRole");
        if (role.getName() == null || role.getName().trim().isEmpty()) {
            bindingResult.addError(new FieldError("newRole", "name", "Tên vai trò không được để trống!"));
        } else if (rolePermissionService.isRoleNameExists(role.getName())) {
            bindingResult.addError(new FieldError("newRole", "name", "Tên vai trò này đã tồn tại!"));
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", rolePermissionService.getAllRoles());
            model.addAttribute("permissionGrouped", rolePermissionService.getPermissionsGroupedByModule());
            model.addAttribute("newPermission", new Permission());
            return "admin/authorization/roles-permissions";
        }

        rolePermissionService.createRole(role);
        redirectAttributes.addFlashAttribute("successMsg", "Thêm vai trò mới thành công!");
        logger.info("end sql");
        return "redirect:/admin/roles-permissions";
    }
    
    @PostMapping("/roles/assign-permissions")
    public String handleAssignPermissions(@RequestParam("roleId") Long roleId,
            @RequestParam(value = "permissionIds", required = false) List<Long> permissionIds,
            RedirectAttributes redirectAttributes) {
        logger.info("start sql handleAssignPermissions");
        rolePermissionService.assignPermissionsToRole(roleId, permissionIds);
        logger.info("end sql");
        redirectAttributes.addFlashAttribute("successMsg", "Cập nhật phân quyền thành công!");
        return "redirect:/admin/roles-permissions?roleId=" + roleId;
    }

    @PostMapping("/permissions/create")
    public String handleCreatePermission(@ModelAttribute("newPermission") Permission permission,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        logger.info("start sql handleCreatePermission");
        if (permission.getName() == null || permission.getName().trim().isEmpty()) {
            bindingResult.addError(new FieldError("newPermission", "name", "Tên quyền không được để trống!"));
        }
        if (permission.getModule() == null || permission.getModule().trim().isEmpty()) {
            bindingResult.addError(new FieldError("newPermission", "module", "Module không được để trống!"));
        }
        if (permission.getApiPath() == null || permission.getApiPath().trim().isEmpty()) {
            bindingResult.addError(new FieldError("newPermission", "apiPath", "Đường dẫn API không được để trống!"));
        } else if (rolePermissionService.isPermissionDuplicate(permission.getApiPath(), permission.getApiMethod())) {
            bindingResult.addError(new FieldError("newPermission", "apiPath", "Cặp (API Path + Method) này đã được cấu hình trước đó!"));
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", rolePermissionService.getAllRoles());
            model.addAttribute("permissionGrouped", rolePermissionService.getPermissionsGroupedByModule());
            model.addAttribute("newRole", new Role());
            return "admin/authorization/roles-permissions";
        }

        rolePermissionService.createPermission(permission);
        redirectAttributes.addFlashAttribute("successMsg", "Tạo quyền hạn mới hệ thống thành công!");
        logger.info("end sql");
        return "redirect:/admin/roles-permissions";
    }
}
