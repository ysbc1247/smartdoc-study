package com.springbootstudy2024.springbootstudy2024.chapter6.listener

import com.springbootstudy2024.springbootstudy2024.chapter6.service.LoginAttemptService
import org.springframework.context.ApplicationListener
import org.springframework.security.authentication.event.AuthenticationSuccessEvent
import org.springframework.stereotype.Service

@Service
class AuthenticationSuccessEventListener(
    private val loginAttemptService: LoginAttemptService,
): ApplicationListener<AuthenticationSuccessEvent> {
    override fun onApplicationEvent(authenticationSuccessEvent: AuthenticationSuccessEvent) {
        // val user = authenticationSuccessEvent.authentication.principal as User
        // loginAttemptService.loginSuccess(user.username)
    }
}
