package com.springbootstudy2024.springbootstudy2024.chapter6.service

import com.springbootstudy2024.springbootstudy2024.chapter6.model.CustomUser
import org.springframework.security.authentication.LockedException
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userService: UserService,
    private val loginAttemptService: LoginAttemptService,
): UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        if (loginAttemptService.isBlocked(username)) {
            throw LockedException("User Account is Locked")
        }

        val user = userService.findByUsername(username)
            ?: throw UsernameNotFoundException("User with username $username not found")
        val simpleGrantedAuthorities =
            if (user.isTotpEnabled) {
                SimpleGrantedAuthority("TOTP_AUTH_AUTHORITY")
            }
            else {
                SimpleGrantedAuthority("ROLE_USER")
            }
        return CustomUser(
            username = user.username,
            password = user.password,
            enabled = true,
            accountNonExpired = true,
            credentialsNonExpired = true,
            accountNonLocked = true,
            authorities = user.authorities.map { SimpleGrantedAuthority(it) } + simpleGrantedAuthorities,
            totpEnabled = user.isTotpEnabled
        )
    }
}
