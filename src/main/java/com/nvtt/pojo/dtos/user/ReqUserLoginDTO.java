package com.nvtt.pojo.dtos.user;

import com.nvtt.utils.validators.LoginChecked;

@LoginChecked
public class ReqUserLoginDTO {

    private String email;
    private String password;

    public ReqUserLoginDTO() {
    }

    public ReqUserLoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
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

    @Override
    public String toString() {
        return this.email;
    }

}
