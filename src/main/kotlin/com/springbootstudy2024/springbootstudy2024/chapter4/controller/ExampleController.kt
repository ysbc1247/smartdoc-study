package com.springbootstudy2024.springbootstudy2024.chapter4.controller

import com.springbootstudy2024.springbootstudy2024.chapter4.service.ExampleService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ExampleController (
    private val exampleService: ExampleService,
){
    @GetMapping("/example")
    fun example(): String {
        exampleService.incrementCounter()
        exampleService.createTimer()
        return "example"
    }
}
