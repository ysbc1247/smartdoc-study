package com.springbootstudy2024.springbootstudy2024.chapter7.main.controller

import com.springbootstudy2024.springbootstudy2024.chapter7.main.model.Course
import com.springbootstudy2024.springbootstudy2024.chapter7.main.service.CourseService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.Optional

@RestController // == @Controller + @ResponseBody
@RequestMapping("/courses/") // 경로에 동사 X, 경로 정의 시 복수형을 쓰는게 좋대요(꼭 그래야하는건 아님)
@Tag(name = "Course Controller", description = "This REST controller provide services to manage courses in the Course Tracker application")
class CourseController(private val courseService: CourseService) {

    // @Controller 사용하면 요기닥가 @ResponseBody를 달아줘야댐
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Provides all courses available in the Course Tracker application")
    fun getAllCourses(): Iterable<Course> = courseService.getCourses()

    @GetMapping("{id}")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Provides course details for the supplied course id from the Course Tracker application")
    fun getCourseById(
        @PathVariable("id")
        courseId: Long
    ): Optional<Course> {
        return courseService.getCourseById(courseId)
    }

    @GetMapping("category/{name}")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Provides course details for the supplied course category from the Course Tracker application")
    fun getCourseByCategory(
        @PathVariable("name")
        category: String
    ): Iterable<Course> {
        return courseService.getCoursesByCategory(category)
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @Operation(summary = "Creates a new course in the Course Tracker application")
    fun createCourse(
        @RequestBody
        course: Course
    ): Course {
        return courseService.createCourse(course)
    }

    @PutMapping("{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @Operation(summary = "Updates the course details in the Course Tracker application for the supplied course id")
    fun updateCourse(
        @PathVariable("id")
        courseId: Long,
        @RequestBody
        course: Course
    ) {
        courseService.updateCourse(courseId, course)
    }

    @DeleteMapping("{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletes the course details for the supplied course id from the Course Tracker application")
    fun deleteCourseById(
        @PathVariable("id")
        courseId: Long
    ) {
        courseService.deleteCourseById(courseId)
    }

    @DeleteMapping
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletes all courses from the Course Tracker application")
    fun deleteCourses() {
        courseService.deleteCourses()
    }
}
