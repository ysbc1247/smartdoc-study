package com.springbootstudy2024.springbootstudy2024.chapter2.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.boot.context.properties.bind.DefaultValue

// Kotlin에서는 아래와 같은 모양으로도 ConfigurationProperty를 가져올 수 있다.
// 모양이 조금 짜치긴 하네요 ㅎ;
@ConfigurationProperties("db2.connection-info")
data class DbConnectionConfigurationData @ConstructorBinding constructor(
    val ip: String,
    val port: String,
    val user: String,
    val password: String,
    // 아래와 같이 설정 값이 없을 때의 기본 값을 선언할 수도 있습니다.
    @DefaultValue("almighty")
    val database: String,
)
