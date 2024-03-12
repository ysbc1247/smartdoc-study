package com.springbootstudy2024.springbootstudy2024.chapter6.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty

data class UserDto(
    @NotEmpty(message = "Enter your firstname")
    val firstName: String? = null,

    @NotEmpty(message = "Enter your lastname")
    val lastName: String? = null,

    @NotEmpty(message = "Enter a username")
    val username: String? = null,

    @NotEmpty(message = "Enter an email")
    @Email(message = "Email is not valid")
    val email: String? = null,

    @NotEmpty(message = "Enter a password")
    val password: String? = null,

    @NotEmpty(message = "Confirm your password")
    val confirmPassword: String? = null,
)
