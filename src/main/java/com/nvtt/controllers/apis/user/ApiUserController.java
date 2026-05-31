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
import com.nvtt.utils.Utilities;
import com.nvtt.utils.exceptions.IdInvalidException;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    
    private static final Logger logger = LogManager.getLogger(ApiUserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/users")
    public ResponseEntity<ResUserInfoDTO> createUser(@RequestParam Map<String, String> params,
            @RequestParam("avatar") MultipartFile avatar) throws IdInvalidException {
        
        if(Utilities.validateRequiredFields(params, "fullName", "email", "password", "role") || avatar.isEmpty()) {
            throw new IdInvalidException("error: missing field");
        }
        logger.info("start sql modelAttribute createUser");
        if (this.userService.checkExistEmail(params.get("email"))) {
            throw new IdInvalidException("This email was exist");
        }
        ResUserInfoDTO dto = this.userService.addUser(params, avatar);
        logger.info("end sql");
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<ResLoginDTO> login(@RequestBody @Valid ReqUserLoginDTO reqUser, BindingResult registerResult) throws IdInvalidException {
        logger.info("start sql login");
        if (registerResult.hasErrors()) {
            throw new IdInvalidException("username/password is missing");
        }
       
        if (!this.userService.checkExistEmail(reqUser.getEmail()) && !this.userService.authenticate(reqUser.getEmail(), reqUser.getPassword())) {
            throw new IdInvalidException("username/password is wrong");
        }
        
        User userDb = this.userService.getUserByEmail(reqUser.getEmail());
        
        if (!userDb.getIsActive()) {
            throw new IdInvalidException("This account was blocked");
        }
        try {
            String accessToken = this.jwtUtil.generateToken(reqUser.getEmail());
            
            ResUserInfoDTO userInfoDto = new ResUserInfoDTO(userDb.getId(), userDb.getEmail(), userDb.getFullName(), userDb.getAvatarUrl(), userDb.getRole().getId(), userDb.getRole().getName(), userDb.getIsActive());

            ResLoginDTO resUser = new ResLoginDTO();
            resUser.setAccessToken(accessToken);
            resUser.setUser(userInfoDto);
            
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(resUser);
        } catch (Exception ex) {
            throw new IdInvalidException("error: creating token");
        } finally {
            logger.info("end sql");
        }
    }

    @GetMapping("/secure/me")
    public ResponseEntity<ResUserInfoDTO> getMyInfo() {
        logger.info("start sql getMyInfo");
        User currentUser = userService.getMyInfo();
        ResUserInfoDTO userInfoDto = new ResUserInfoDTO(currentUser.getId(), currentUser.getEmail(),
                currentUser.getFullName(), currentUser.getAvatarUrl(), currentUser.getRole().getId(), currentUser.getRole().getName(), currentUser.getIsActive());
        logger.info("end sql");
        return ResponseEntity.status(HttpStatus.OK).body(userInfoDto);

    }

    @PutMapping("/secure/me")
    public ResponseEntity<ResUserInfoDTO> updateMyInfo(@RequestParam Map<String, String> params,
            @RequestParam("avatar") Optional<MultipartFile> avatar) {
        logger.info("start sql updateMyInfo");
        ResUserInfoDTO userInfoDto = userService.updateMyInfo(params, avatar);
        logger.info("end sql");
        return ResponseEntity.status(HttpStatus.OK).body(userInfoDto);
    }
}
