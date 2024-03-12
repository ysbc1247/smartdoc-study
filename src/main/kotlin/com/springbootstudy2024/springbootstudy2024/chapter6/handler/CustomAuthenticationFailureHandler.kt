package com.springbootstudy2024.springbootstudy2024.chapter6.handler

import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.DefaultRedirectStrategy
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Service
import java.io.IOException

@Service
class CustomAuthenticationFailureHandler: AuthenticationFailureHandler {
    private val defaultRedirectStrategy = DefaultRedirectStrategy()

    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationFailure(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        exception: AuthenticationException
    ) {
        if (exception.cause is LockedException) {
            defaultRedirectStrategy.sendRedirect(request, response, "/login-locked")
            return
        }

        defaultRedirectStrategy.sendRedirect(request, response, "/login-error")
    }
}
