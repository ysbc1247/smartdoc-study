import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.4"

    val kotlinVersion = "1.7.20"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
    kotlin("kapt") version kotlinVersion
}

group = "com.springbootstudy2024"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

springBoot {
    buildInfo()
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web"){
        exclude(group = "ch.qos.logback", module = "logback-classic")
    }
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.springframework.boot:spring-boot-starter-logging")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // 2.5장: Bean Validation
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // 3장 데이터베이스
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.h2database:h2")

    // 4장 액추에이터
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")

    // 5장 스프링 시큐리티
    implementation("org.springframework.boot:spring-boot-starter-security")

    // 6장 스프링 시큐리티 응용
    implementation("com.google.guava:guava:30.1.1-jre")
    implementation("com.warrenstrange:googleauth:1.4.0")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

    // 7장 스프링 부트 RESTful 웹 서비스 개발
    implementation("org.springdoc", "springdoc-openapi-starter-common", "2.2.0")
    implementation("org.springdoc", "springdoc-openapi-starter-webmvc-ui", "2.2.0")


}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

