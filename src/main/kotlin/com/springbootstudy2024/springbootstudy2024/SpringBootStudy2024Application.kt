package com.springbootstudy2024.springbootstudy2024

import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableAsync
import java.time.LocalDate
import java.util.Properties

@SpringBootApplication
@EnableAsync
class SpringBootStudy2024Application

fun main(args: Array<String>) {
    runApplication<SpringBootStudy2024Application>(*args)
}

