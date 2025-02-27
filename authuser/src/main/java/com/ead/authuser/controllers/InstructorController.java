package com.ead.authuser.controllers;

import com.ead.authuser.dtos.InstructorRecordDto;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/instructors")
public class InstructorController {
    private final UserService userService;

    @PostMapping("/subscription")
    public ResponseEntity<Object> saveSubscriptionInstructor(
            @RequestBody @Valid InstructorRecordDto instructorRecordDto) {
        UserModel userModel = userService.findById(instructorRecordDto.userID());

        return ResponseEntity.status(HttpStatus.OK).body(userService.registerInstructor(userModel));
    }
}
