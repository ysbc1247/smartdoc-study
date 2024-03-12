package com.springbootstudy2024.springbootstudy2024.chapter2.validation

import jakarta.validation.ConstraintViolation
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class CourseValidationCommandLineRunner: CommandLineRunner {

    private val logger = LoggerFactory.getLogger(this::class.java.name)

    override fun run(vararg args: String?) {

        val course = Course(
            id = 1,
            name = "이세계 아이돌 입문",
            category = "Culture",
            rating = 10,
            description = "전능 최고의 강사 진수민과 함께하는 이세돌의 역사",
            studentCapacity = 101,
        )

        val validator: Validator = Validation.buildDefaultValidatorFactory().validator
        val violations: Set<ConstraintViolation<Course>> = validator.validate(course)

        if (violations.isNotEmpty()) {
            logger.error("Course validation failed!")
            violations.forEach { violation ->
                logger.error("${violation.message} --- 입력값: ${violation.invalidValue}")
            }
        }
    }
}
