/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.utils.validators;

import com.nvtt.pojo.dtos.admin.RegisterRequestDTO;
import com.nvtt.pojo.dtos.user.ReqUserLoginDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Service;

/**
 *
 * @author vthan
 */
@Service
public class LoginValidator implements ConstraintValidator<LoginChecked, ReqUserLoginDTO> {

    @Override
    public boolean isValid(ReqUserLoginDTO user, ConstraintValidatorContext context) {
        boolean valid = true;
        if (user.getPassword().isBlank()) {
            context.buildConstraintViolationWithTemplate("Email is missed")
                    .addPropertyNode("email")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            valid = false;
        }

        if (user.getEmail().isBlank()) {
            context.buildConstraintViolationWithTemplate("Password is missed")
                    .addPropertyNode("password")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            valid = false;
        }

        return valid;
    }
}
