package com.springbootstudy2024.springbootstudy2024.chapter7.main.exception.handler

import com.springbootstudy2024.springbootstudy2024.chapter7.main.exception.CourseNotFoundException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class CourseTrackerGlobalExceptionHandler: ResponseEntityExceptionHandler() {
    @ExceptionHandler(value = [CourseNotFoundException::class])
    fun handleCourseNotFound(
        courseNotFoundException: CourseNotFoundException,
        request: WebRequest?
    ): ResponseEntity<*>? {
        return handleExceptionInternal(
            courseNotFoundException,
            courseNotFoundException.message, HttpHeaders(), HttpStatus.NOT_FOUND,
            request!!,
        )
    }
}
