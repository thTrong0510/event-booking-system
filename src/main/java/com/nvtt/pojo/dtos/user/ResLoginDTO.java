/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.pojo.dtos.user;

/**
 *
 * @author vthan
 */
public class ResLoginDTO {
    private String accessToken;
    private ResUserInfoDTO user;

    public ResLoginDTO() {
    }

    public ResLoginDTO(String accessToken, ResUserInfoDTO user) {
        this.accessToken = accessToken;
        this.user = user;
    }
    

    /**
     * @return the accessToken
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * @param accessToken the accessToken to set
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * @return the user
     */
    public ResUserInfoDTO getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(ResUserInfoDTO user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return this.user.getEmail();
    }
    
}
