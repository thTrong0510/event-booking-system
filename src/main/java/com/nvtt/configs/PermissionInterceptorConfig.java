/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.configs;

import com.nvtt.pojo.Permission;
import com.nvtt.pojo.Role;
import com.nvtt.pojo.User;
import com.nvtt.services.UserService;
import com.nvtt.utils.SecurityUtil;
import com.nvtt.utils.exceptions.PermissionException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

/**
 *
 * @author vthan
 */
public class PermissionInterceptorConfig implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response, Object handler)
            throws Exception {

        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();
        System.out.println(">>> RUN preHandle");
        System.out.println(">>> path= " + path);
        System.out.println(">>> httpMethod= " + httpMethod);
        System.out.println(">>> requestURI= " + requestURI);

        // check permission
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        if (email != null && !email.isEmpty()) {
            User currentUser = this.userService.getUserByEmail(email);
            if (currentUser != null) {
                Role role = currentUser.getRole();
                if (role != null) {
                    Set<Permission> permissions = role.getPermissions();
                    if (permissions != null && !permissions.isEmpty()) {
                        Boolean isAllow = permissions
                                .stream().anyMatch(
                                        item -> item.getApiPath().equals(path) && item.getApiMethod().equals(httpMethod));
                        if (!isAllow) {
                            throw new PermissionException("Have no Permission to access feature");
                        }
                    }
                } else {
                    throw new PermissionException("Have no Permission to access feature");

                }

            }
        }

        return true;
    }
}
