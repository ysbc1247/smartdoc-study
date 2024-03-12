package com.springbootstudy2024.springbootstudy2024.chapter9.main

import com.springbootstudy2024.springbootstudy2024.SpringBootStudy2024Application
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer

class ServletInitializer: SpringBootServletInitializer() {
    override fun configure(application: SpringApplicationBuilder): SpringApplicationBuilder {
        return application.sources(SpringBootStudy2024Application::class.java)
    }
}
