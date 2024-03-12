package com.springbootstudy2024.springbootstudy2024.chapter7.main.repository

import com.springbootstudy2024.springbootstudy2024.chapter7.main.model.Course
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CourseRepository: CrudRepository<Course, Long> {
    fun findAllByCategory(category: String): Iterable<Course>
}
