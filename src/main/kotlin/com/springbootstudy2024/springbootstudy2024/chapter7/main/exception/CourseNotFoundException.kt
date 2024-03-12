package com.springbootstudy2024.springbootstudy2024.chapter7.main.exception

class CourseNotFoundException(message: String): RuntimeException(message) {

    companion object {
        private const val serialVersionUID = 5071646428281007896L
    }
}
