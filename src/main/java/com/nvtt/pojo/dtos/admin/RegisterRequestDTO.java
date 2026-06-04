package com.nvtt.pojo.dtos.admin;

import com.nvtt.utils.validators.RegisterChecked;
import org.springframework.web.multipart.MultipartFile;

@RegisterChecked
public class RegisterRequestDTO {

    private String fullName;
    private String email;
    private String password;
    private String confirmPassword;
    private MultipartFile avatarUrl;
    private Long roleId;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public MultipartFile getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(MultipartFile avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
