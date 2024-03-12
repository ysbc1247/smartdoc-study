package com.springbootstudy2024.springbootstudy2024.chapter6.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.DefaultRedirectStrategy
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Service

@Service
class DefaultAuthenticationSuccessHandler: AuthenticationSuccessHandler {
    private val defaultRedirectStrategy = DefaultRedirectStrategy()

    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {
        if(isTotpAuthRequired(authentication)) {
            defaultRedirectStrategy.sendRedirect(request, response, "/totp-login")
        }
        else {
            defaultRedirectStrategy.sendRedirect(request, response, "/account")
        }
    }

    private fun isTotpAuthRequired(authentication: Authentication?): Boolean {
        return authentication?.authorities?.any { it.authority == "TOTP_AUTH_AUTHORITY" } ?: false
    }
}
