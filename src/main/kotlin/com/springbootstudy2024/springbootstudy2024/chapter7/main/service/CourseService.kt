package com.springbootstudy2024.springbootstudy2024.chapter7.main.service

import com.springbootstudy2024.springbootstudy2024.chapter7.main.model.Course
import java.util.Optional

interface CourseService {
    fun createCourse(course: Course): Course
    fun getCourseById(courseId: Long): Optional<Course>
    fun getCoursesByCategory(category: String): Iterable<Course>
    fun getCourses(): Iterable<Course>
    fun updateCourse(courseId: Long, course: Course)
    fun deleteCourseById(courseId: Long)
    fun deleteCourses()
}
