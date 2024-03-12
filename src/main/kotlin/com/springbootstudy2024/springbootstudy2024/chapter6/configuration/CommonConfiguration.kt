package com.springbootstudy2024.springbootstudy2024.chapter6.configuration

import com.springbootstudy2024.springbootstudy2024.chapter6.handler.CustomAuthenticationFailureHandler
import jakarta.servlet.DispatcherType
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.AdviceMode
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.firewall.HttpFirewall
import org.springframework.security.web.firewall.StrictHttpFirewall
import org.springframework.web.client.RestTemplate

@Configuration
class CommonConfiguration {
  @Bean
  fun restTemplate(restTemplateBuilder: RestTemplateBuilder): RestTemplate {
    return restTemplateBuilder.build()
  }
}
