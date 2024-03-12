package com.springbootstudy2024.springbootstudy2024.chapter6.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.DefaultRedirectStrategy
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class Oauth2AuthenticationSuccessHandler: AuthenticationSuccessHandler {
    private val defaultRedirectStrategy = DefaultRedirectStrategy()
    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {
        defaultRedirectStrategy.sendRedirect(request, response, "/index")
    }
}
