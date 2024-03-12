package com.springbootstudy2024.springbootstudy2024.chapter6.configuration

import com.springbootstudy2024.springbootstudy2024.chapter6.filter.TotpAuthFilter
import com.springbootstudy2024.springbootstudy2024.chapter6.handler.CustomAuthenticationFailureHandler
import com.springbootstudy2024.springbootstudy2024.chapter6.handler.Oauth2AuthenticationSuccessHandler
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfiguration(
    private val customAuthenticationFailureHandler: CustomAuthenticationFailureHandler,
    private val totpAuthFilter: TotpAuthFilter,
) {
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .cors {}
            .csrf {
                it.disable()
            }
            .headers {
                it.frameOptions {
                    it.disable()
                }
            }
            // Chapter6.2 모든 요청에 HTTPS 적용하기
            // .requiresChannel{
            //     it.anyRequest().requiresSecure()
            // }
            .addFilterBefore(
                totpAuthFilter,
                UsernamePasswordAuthenticationFilter::class.java
            )
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers("/adduser", "/login", "/login-error", "/login-locked").permitAll()
                    .requestMatchers("/webjars/**", "/css/**", "/h2-console/**", "/images/**").permitAll()
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                    .requestMatchers("/courses/**", "/example").permitAll()
                    .requestMatchers("/totp-login", "totp-login-error").hasAuthority("TOTP_AUTH_AUTHORITY")
                    .requestMatchers(EndpointRequest.to("health")).hasAnyRole("USER", "ADMIN")
                    .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ADMIN")
                    .anyRequest().authenticated()
            }
            .formLogin {
                it.loginPage("/login")
                    .defaultSuccessUrl("/index", true).permitAll()
                    .failureHandler(customAuthenticationFailureHandler)
            }
            // Chapter 6.6 Remember me 기능 구현하기
            .rememberMe {
                it.key("remember-me-key").rememberMeCookieName("course-tracker-remember-me")
            }
            .logout {
                it.deleteCookies("course-tracker-remember-me")
            }
            .oauth2Login {
                it.loginPage("/login")
                    .successHandler(Oauth2AuthenticationSuccessHandler())
            }
            .build()
    }
}

