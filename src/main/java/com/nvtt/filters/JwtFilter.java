package com.nvtt.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.proc.ExpiredJWTException;
import com.nvtt.utils.JwtUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author vthan
 */
public class JwtFilter implements Filter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  httpReq = (HttpServletRequest)  request;
        HttpServletResponse httpRes = (HttpServletResponse) response;

        if (httpReq.getRequestURI().startsWith(String.format("%s/api/secure", httpReq.getContextPath())) == true) {

            String header = httpReq.getHeader("Authorization");

            if (header == null || !header.startsWith("Bearer ")) {
                writeJsonError(httpRes, HttpServletResponse.SC_UNAUTHORIZED,
                        "MISSING_TOKEN",
                        "Missing or invalid Authorization header.");
                return;
            }

            String token = header.substring(7);
            try {
                String username = JwtUtil.validateTokenAndGetUsername(token);
                if (username != null) {
                    httpReq.setAttribute("username", username);
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(username, null, null);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    chain.doFilter(request, response);
                    return;
                } else {
                    writeJsonError(httpRes, HttpServletResponse.SC_UNAUTHORIZED,
                        "TOKEN_INVALID",
                        "Token không hợp lệ.");
                return;

                }
                
            } catch (ExpiredJWTException e) {
                writeJsonError(httpRes, HttpServletResponse.SC_UNAUTHORIZED,
                        "TOKEN_EXPIRED",
                        "Token đã hết hạn, vui lòng đăng nhập lại.");
                return;
            } catch (Exception e) {
                writeJsonError(httpRes, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "SERVER_ERROR",
                        "Lỗi xác thực không xác định.");
                return;
            }

            // username == null nhưng không throw exception
//            writeJsonError(httpRes, HttpServletResponse.SC_UNAUTHORIZED,
//                    "TOKEN_INVALID",
//                    "Token không hợp lệ hoặc hết hạn.");
//            return;
        }

        chain.doFilter(request, response);
    }

    // -------------------------------------------------------
    // Helper: ghi thẳng JSON vào response, KHÔNG qua Tomcat
    // -------------------------------------------------------
    private void writeJsonError(HttpServletResponse response,
                                int statusCode,
                                String errorCode,
                                String message) throws IOException {

        // Phải set trước khi getWriter()
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("statusCode",    statusCode);
        body.put("error",     errorCode);
        body.put("message",   message);
        body.put("data", null);

        response.getWriter().write(objectMapper.writeValueAsString(body));
        response.getWriter().flush();
    }
}