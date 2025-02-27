package com.ead.authuser.controllers;

import com.ead.authuser.clients.CourseClient;
import com.ead.authuser.dtos.CourseRecordDto;
import com.ead.authuser.dtos.UserCourseRecordDto;
import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserCourseService;
import com.ead.authuser.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserCourseController {

    final private CourseClient courseClient;
    final private UserService userService;
    final private UserCourseService userCourseService;

    @GetMapping("/users/{userId}/courses")
    public ResponseEntity<Page<CourseRecordDto>> getAllCoursesByUser(
            @PageableDefault(sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable,
            @PathVariable(value = "userId") UUID userId) {
        return ResponseEntity.status(HttpStatus.OK).body(courseClient.getAllCourseByUser(userId, pageable));
    }

    @PostMapping("/users/{userId}/courses/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(
            @PathVariable(value = "userId") UUID userId,
            @RequestBody @Valid UserCourseRecordDto userCourseRecordDto) {
        UserModel userModel = userService.findById(userId);

        if(userCourseService.existsByUserAndCourseId(userModel, userCourseRecordDto.courseId())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Subscription already exists!");
        }

        UserCourseModel userCourseModel = userModel.convertToUserCourseModel(userCourseRecordDto.courseId());

        return ResponseEntity.status(HttpStatus.CREATED).body(userCourseService.save(userCourseModel));
    }
}
