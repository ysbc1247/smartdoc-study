package com.springbootstudy2024.springbootstudy2024.chapter6.service

import com.springbootstudy2024.springbootstudy2024.chapter6.dto.UserDto
import com.springbootstudy2024.springbootstudy2024.chapter6.model.ApplicationUser
import com.springbootstudy2024.springbootstudy2024.chapter6.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    fun createUser(userDto: UserDto) {
        val user = ApplicationUser(
            firstName = userDto.firstName,
            lastName = userDto.lastName,
            username = userDto.username,
            email = userDto.email,
            password = passwordEncoder.encode(userDto.password),
            isTotpEnabled = false,
            authorities = if (userDto.username == "admin") listOf("ROLE_ADMIN") else emptyList(),
        )

        userRepository.save(user)
    }

    fun save(user: ApplicationUser) {
        userRepository.save(user)
    }

    fun findByUsername(username: String): ApplicationUser? {
        return userRepository.findByUsername(username)
    }
}
