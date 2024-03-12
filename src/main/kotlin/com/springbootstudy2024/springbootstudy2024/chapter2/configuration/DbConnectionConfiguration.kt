package com.springbootstudy2024.springbootstudy2024.chapter2.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("db.connection-info")
class DbConnectionConfiguration(
    private val dbConnectionConfigurationData: DbConnectionConfigurationData
) {
    lateinit var ip: String
    lateinit var port: String
    lateinit var user: String
    lateinit var password: String
    fun getData(): String {
        return "$ip + $port + $user + $password + $dbConnectionConfigurationData"
    }
}
