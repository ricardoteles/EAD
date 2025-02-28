package com.ead.authuser.validations;

import com.ead.authuser.dtos.UserRecordDto;
import com.ead.authuser.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
@Component
public class UserValidatior implements Validator {
    Logger logger = LogManager.getLogger(UserValidatior.class);

    private final Validator validator;
    private final UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserRecordDto userRecordDto = (UserRecordDto) o;
        validator.validate(userRecordDto, errors);

        if(!errors.hasErrors()) {
            validateUsername(userRecordDto, errors);
            validateEmail(userRecordDto, errors);
        }
    }

    private void validateUsername(UserRecordDto userRecordDto, Errors errors){
        if(userService.existsByUsername(userRecordDto.username())) {
            errors.rejectValue("username", "usernameConflict", "Error: Username is already taken!");
            logger.error("Error validation username: {}", userRecordDto.username());
        }
    }

    private void validateEmail(UserRecordDto userRecordDto, Errors errors){
        if(userService.existsByEmail(userRecordDto.email())) {
            errors.rejectValue("email", "emailConflict", "Error: Email is already taken!");
            logger.error("Error validation email: {}", userRecordDto.email());
        }
    }
}
