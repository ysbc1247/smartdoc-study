package com.springbootstudy2024.springbootstudy2024.chapter3.database

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("spring.datasource")
data class SpringDataSourceConfig(
    val url: String,
    val driverClassName: String,
    val username: String,
    val password: String,
)
