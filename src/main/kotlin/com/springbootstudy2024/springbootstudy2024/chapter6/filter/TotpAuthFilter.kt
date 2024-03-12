package com.springbootstudy2024.springbootstudy2024.chapter6.filter

import com.springbootstudy2024.springbootstudy2024.chapter6.service.TotpService
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.DefaultRedirectStrategy
import org.springframework.security.web.RedirectStrategy
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean

@Component
class TotpAuthFilter (
    private val totpService: TotpService
): GenericFilterBean() {
    private val redirectStrategy: RedirectStrategy = DefaultRedirectStrategy()
    override fun doFilter(request: ServletRequest, response: ServletResponse?, chain: FilterChain) {
        var authentication = SecurityContextHolder.getContext().authentication
        val code: String? = request.getParameter("totp_code")
        if (!requiresTotpAuthentication(authentication) || code == null) {
            chain.doFilter(request, response)
            return
        }
        if (code != null && totpService.verifyCode(authentication.name, code.toInt())) {
            val authorities = AuthorityUtils.authorityListToSet(authentication.authorities)
            authorities.remove("TOTP_AUTH_AUTHORITY")
            authorities.add("ROLE_USER")
            authentication = UsernamePasswordAuthenticationToken(
                authentication.principal,
                authentication.credentials,
                buildAuthorities(authorities),
            )
            SecurityContextHolder.getContext().authentication = authentication
            redirectStrategy.sendRedirect(
                request as HttpServletRequest,
                response as HttpServletResponse?,
                ON_SUCCESS_URL,
            )
        }
        else {
            redirectStrategy.sendRedirect(
                request as HttpServletRequest,
                response as HttpServletResponse?,
                ON_FAILURE_URL,
            )
        }
    }

    private fun requiresTotpAuthentication(authentication: Authentication?): Boolean {
        if (authentication == null) {
            return false
        }
        val authorities = AuthorityUtils.authorityListToSet(authentication.authorities)
        val hasTotpAutheority = authorities.contains("TOTP_AUTH_AUTHORITY")
        return hasTotpAutheority && authentication.isAuthenticated
    }

    private fun buildAuthorities(authorities: Collection<String>): List<GrantedAuthority> {
        val authList: MutableList<GrantedAuthority> = ArrayList(1)
        for (authority in authorities) {
            authList.add(SimpleGrantedAuthority(authority))
        }
        return authList
    }

    companion object {
        private const val ON_SUCCESS_URL = "/index"
        private const val ON_FAILURE_URL = "/totp-login-error"
    }
}
