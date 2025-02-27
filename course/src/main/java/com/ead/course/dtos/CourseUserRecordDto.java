package com.ead.course.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CourseUserRecordDto(
        UUID courseId,
        UUID userId) {
}
