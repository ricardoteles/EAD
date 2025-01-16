package com.ead.course.controllers;

import com.ead.course.dtos.ModuleRecordDto;
import com.ead.course.services.CourseService;
import com.ead.course.services.ModuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class ModuleController {
    private final ModuleService moduleService;
    private final CourseService courseService;

    @PostMapping("/courses/{courseId}/modules")
    public ResponseEntity<Object> saveModule(@PathVariable(value = "courseId") UUID courseId,
                                             @RequestBody @Valid ModuleRecordDto moduleRecordDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(moduleService.save(moduleRecordDto, courseService.findById(courseId).get()));
    }


}
