package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserRecordDto;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.ead.authuser.specifications.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
//@CrossOrigin(origins = "*", maxAge = 3600)                // example of cross-origin at the class level
public class UserController {
    Logger logger = LogManager.getLogger(UserController.class);

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserModel>> getAllUsers(SpecificationTemplate.UserSpec spec, Pageable pageable) {
        Page<UserModel> userModelPage = userService.findAll(spec, pageable);

        if(!userModelPage.isEmpty()) {
            for(UserModel user : userModelPage.toList()) {
                user.add(linkTo(methodOn(UserController.class).getOneUser(user.getUserId())).withSelfRel());
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(userModelPage);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getOneUser(@PathVariable(value = "userId") UUID userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findById(userId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "userId") UUID userId) {
        logger.debug("DELETE deleteUser userId received {}", userId);
        userService.delete(userService.findById(userId));
        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully.");
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "userId") UUID userId,
                                             @RequestBody @Validated(UserRecordDto.UserView.UserPut.class)
                                             @JsonView(UserRecordDto.UserView.UserPut.class)
                                            UserRecordDto userRecordDto) {
        logger.debug("PUT updateUser userRecordDto received {}", userRecordDto);
        var userModel = userService.findById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userRecordDto, userModel));
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Object> updatePassword(@PathVariable(value = "userId") UUID userId,
                                             @RequestBody @Validated(UserRecordDto.UserView.PasswordPut.class)
                                             @JsonView(UserRecordDto.UserView.PasswordPut.class)
                                             UserRecordDto userRecordDto) {
        logger.debug("PUT updatePassword userRecordDto received {}", userRecordDto);
        var userModel = userService.findById(userId);

        if(!userModel.getPassword().equals(userRecordDto.oldPassword())) {
            logger.warn(" Mismatched old password! userId {}", userId);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Mismatched old password!");
        }

        userService.updatePassword(userRecordDto, userModel);

        return ResponseEntity.status(HttpStatus.OK).body("Password updated successfully.");
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<Object> updateImage(@PathVariable(value = "userId") UUID userId,
                                             @RequestBody @Validated(UserRecordDto.UserView.ImagePut.class)
                                             @JsonView(UserRecordDto.UserView.ImagePut.class)
                                             UserRecordDto userRecordDto) {
        logger.debug("PUT updateImage userRecordDto received {}", userRecordDto);
        var userModel = userService.findById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateImage(userRecordDto, userModel));
    }
}
