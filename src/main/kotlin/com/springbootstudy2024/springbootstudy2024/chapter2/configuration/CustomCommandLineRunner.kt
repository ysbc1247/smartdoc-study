package com.springbootstudy2024.springbootstudy2024.chapter2.configuration

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

// Component를 이용한 구현
@Component
@Order(1)
class CustomCommandLineRunner: CommandLineRunner {
    val logger = LoggerFactory.getLogger(this::class.java.name)

    override fun run(vararg args: String?) {
        logger.info("CommandLineRunner with Component executed!")
    }
}
