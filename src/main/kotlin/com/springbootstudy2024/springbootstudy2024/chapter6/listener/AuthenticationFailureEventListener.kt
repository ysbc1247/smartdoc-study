package com.springbootstudy2024.springbootstudy2024.chapter6.listener

import com.springbootstudy2024.springbootstudy2024.chapter6.service.LoginAttemptService
import org.springframework.context.ApplicationListener
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent
import org.springframework.stereotype.Service

@Service
class AuthenticationFailureEventListener(
    private val loginAttemptService: LoginAttemptService,
): ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
    override fun onApplicationEvent(authenticationFailureBadCredentialsEvent: AuthenticationFailureBadCredentialsEvent) {
        val username = authenticationFailureBadCredentialsEvent.authentication.principal as String
        loginAttemptService.loginFailed(username)
    }
}
