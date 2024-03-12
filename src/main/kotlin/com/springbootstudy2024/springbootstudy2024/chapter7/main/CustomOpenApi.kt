package com.springbootstudy2024.springbootstudy2024.chapter7.main

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CustomOpenApi {
    @Bean
    fun customOpenAPI(
        @Value("\${info.app.description}")
        appDescription: String?,
        @Value("\${info.app.version}")
        appVersion: String?
    ): OpenAPI {
        return OpenAPI().info(
            Info().title("Course Tracker API").version(appVersion)
                .description(appDescription).termsOfService("http://swagger.io/terms/")
                .license(License().name("Apache 2.0").url("http://springdoc.org")),
        )
    }
}
