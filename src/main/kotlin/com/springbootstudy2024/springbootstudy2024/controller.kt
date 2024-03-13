package com.springbootstudy2024.springbootstudy2024

import org.springframework.web.bind.annotation.RestController

@RestController
class controller {
    fun hello(): String {
        return "Hello, Spring Boot!"
    }
}