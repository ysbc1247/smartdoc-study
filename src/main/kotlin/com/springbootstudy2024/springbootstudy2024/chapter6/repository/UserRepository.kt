package com.springbootstudy2024.springbootstudy2024.chapter6.repository

import com.springbootstudy2024.springbootstudy2024.chapter6.model.ApplicationUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<ApplicationUser, Long> {
    fun findByUsername(username: String): ApplicationUser?
}
