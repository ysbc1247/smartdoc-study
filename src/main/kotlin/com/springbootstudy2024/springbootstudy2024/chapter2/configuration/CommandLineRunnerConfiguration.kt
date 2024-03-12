package com.springbootstudy2024.springbootstudy2024.chapter2.configuration

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order

@Configuration
class CommandLineRunnerConfiguration {
    val logger = LoggerFactory.getLogger(this::class.java.name)

    // Bean을 이용한 구현
    @Bean
    @Order(100)
    fun commandLineRunner() = CommandLineRunner { args ->
        logger.info("CommandLineRunner from Bean executed!")
    }
}
