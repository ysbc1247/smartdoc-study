package com.springbootstudy2024.springbootstudy2024.chapter3.database

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class SpringDataSourceConfigCommandLineRunner(
    private val springDataSourceConfig: SpringDataSourceConfig,
): CommandLineRunner {

    private val logger = LoggerFactory.getLogger(this::class.java.name)

    override fun run(vararg args: String?) {

        logger.info(
            """
            |SpringDataSourceConfigCommandLineRunner
            |url: ${springDataSourceConfig.url}
            |driverClassName: ${springDataSourceConfig.driverClassName}
            |username: ${springDataSourceConfig.username}
            |password: ${springDataSourceConfig.password}
        """.trimIndent(),
        )
    }
}
