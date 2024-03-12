package com.springbootstudy2024.springbootstudy2024.chapter2.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment

@Configuration
// yaml을 쓰고싶다면, PropertySourceFactory를 implement하는 임의의 YamlPropertySourceFactory를 직접 구축해줘야 합니다.
// https://www.baeldung.com/spring-yaml-propertysource 참조
@PropertySource("classpath:property-source-test.properties")
class DbConfiguration(
    private val environment: Environment
) {
    fun getData(): String {
        return "User: ${environment.getProperty("user")}, Password: ${environment.getProperty("password")}"
    }
}
