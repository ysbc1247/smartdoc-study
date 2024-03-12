package com.springbootstudy2024.springbootstudy2024.chapter6.dto

import com.sun.net.httpserver.Authenticator.Success
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty

data class RecaptchaDto(
    val success:Boolean,
    val errors: List<String>
)
