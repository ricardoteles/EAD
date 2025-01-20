package com.ead.course.controllers;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

public record LessonRecordDto(
        @NotBlank
        String title,
        @NotBlank
        String description,
        @NotBlank
        String videoUrl
) {
}
