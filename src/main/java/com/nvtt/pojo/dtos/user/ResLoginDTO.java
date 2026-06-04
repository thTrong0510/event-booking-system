package com.nvtt.pojo.dtos.user;

public class ResLoginDTO {

    private String accessToken;
    private ResUserInfoDTO user;

    public ResLoginDTO() {
    }

    public ResLoginDTO(String accessToken, ResUserInfoDTO user) {
        this.accessToken = accessToken;
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public ResUserInfoDTO getUser() {
        return user;
    }

    public void setUser(ResUserInfoDTO user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return this.user.getEmail();
    }

}
