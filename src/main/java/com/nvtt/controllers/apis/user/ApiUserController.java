/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.controllers.apis.user;

import com.nvtt.pojo.User;
import com.nvtt.pojo.dtos.user.ReqUserLoginDTO;
import com.nvtt.pojo.dtos.user.ResLoginDTO;
import com.nvtt.pojo.dtos.user.ResUserInfoDTO;
import com.nvtt.services.UserService;
import com.nvtt.utils.JwtUtil;
import com.nvtt.utils.exceptions.IdInvalidException;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author vthan
 */
@RestController
@RequestMapping("/api")
public class ApiUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/users")
    public ResponseEntity<ResUserInfoDTO> createUser(@RequestParam Map<String, String> params,
            @RequestParam("avatar") MultipartFile avatar) throws IdInvalidException {
        if(this.userService.checkExistEmail(params.get("email"))) {
            throw new IdInvalidException("This email was exist");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.addUser(params, avatar));
    }

    @PostMapping("/login")
    public ResponseEntity<ResLoginDTO> login(@RequestBody ReqUserLoginDTO reqUser) throws IdInvalidException {
        if (!this.userService.authenticate(reqUser.getEmail(), reqUser.getPassword())) {
            throw new IdInvalidException("username/password is wrong");
        }
        if (!this.userService.checkActiveAccount(reqUser.getEmail())) {
            throw new IdInvalidException("This account was blocked");
        }
        try {
            String accessToken = this.jwtUtil.generateToken(reqUser.getEmail());

            User userdb = this.userService.getUserByEmail(reqUser.getEmail());
            ResUserInfoDTO userInfoDto = new ResUserInfoDTO(userdb.getId(), userdb.getEmail(), userdb.getFullName(), userdb.getAvatarUrl(), userdb.getRole().getId(), userdb.getRole().getName(), userdb.getIsActive());

            ResLoginDTO resUser = new ResLoginDTO();
            resUser.setAccessToken(accessToken);
            resUser.setUser(userInfoDto);

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(resUser);
        } catch (Exception ex) {
            throw new IdInvalidException("error: creating token");
        }
    }
    
    @GetMapping("/secure/me")
    public ResponseEntity<ResUserInfoDTO> getMyInfo() {
        try {
            User currentUser = userService.getMyInfo();
            ResUserInfoDTO userInfoDto = new ResUserInfoDTO(currentUser.getId(), currentUser.getEmail(),
                    currentUser.getFullName(), currentUser.getAvatarUrl(), currentUser.getRole().getName());
            return ResponseEntity.status(HttpStatus.OK).body(userInfoDto);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/secure/me")
    public ResponseEntity<ResUserInfoDTO> updateMyInfo(@RequestParam Map<String, String> params,
            @RequestParam("avatar") Optional<MultipartFile> avatar) {
        try {
            ResUserInfoDTO userInfoDto = userService.updateMyInfo(params, avatar);
            return ResponseEntity.status(HttpStatus.OK).body(userInfoDto);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
