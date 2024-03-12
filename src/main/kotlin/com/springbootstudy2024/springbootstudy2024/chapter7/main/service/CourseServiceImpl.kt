package com.springbootstudy2024.springbootstudy2024.chapter7.main.service

import com.springbootstudy2024.springbootstudy2024.chapter7.main.exception.CourseNotFoundException
import com.springbootstudy2024.springbootstudy2024.chapter7.main.model.Course
import com.springbootstudy2024.springbootstudy2024.chapter7.main.repository.CourseRepository
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class CourseServiceImpl(private val courseRepository: CourseRepository): CourseService {

    override fun createCourse(course: Course): Course {
        return courseRepository.save(course)
    }

    override fun getCourseById(courseId: Long): Optional<Course> {
        return courseRepository.findById(courseId)
    }

    override fun getCoursesByCategory(category: String): Iterable<Course> {
        return courseRepository.findAllByCategory(category)
    }

    override fun getCourses(): Iterable<Course> = courseRepository.findAll()

    override fun updateCourse(courseId: Long, course: Course) {
        // courseRepository.findById(courseId).ifPresent { dbCourse ->
        //     dbCourse.name = course.name
        //     dbCourse.category = course.category
        //     dbCourse.description = course.category
        //     dbCourse.rating = course.rating
        //     courseRepository.save(dbCourse)
        // }
        val existingCourse = courseRepository.findById(courseId)
            .orElseThrow { CourseNotFoundException("No course with id $courseId is available") }
        existingCourse.name = course.name
        existingCourse.category = course.category
        existingCourse.description = course.category
        existingCourse.rating = course.rating
        courseRepository.save(existingCourse)
    }

    override fun deleteCourses() {
        courseRepository.deleteAll()
    }

    override fun deleteCourseById(courseId: Long) {
        // courseRepository.deleteById(courseId)

        courseRepository.findById(courseId)
            .orElseThrow { CourseNotFoundException("No course with id $courseId is available") }
        courseRepository.deleteById(courseId)
    }
}
