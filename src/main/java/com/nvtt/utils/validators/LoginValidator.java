package com.nvtt.utils.validators;

import com.nvtt.pojo.dtos.user.ReqUserLoginDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Service;

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
