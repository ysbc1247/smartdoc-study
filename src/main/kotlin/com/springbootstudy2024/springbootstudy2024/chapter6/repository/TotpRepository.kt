package com.springbootstudy2024.springbootstudy2024.chapter6.repository

import com.springbootstudy2024.springbootstudy2024.chapter6.model.TotpDetails
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TotpRepository: JpaRepository<TotpDetails, Long> {
    fun deleteByUsername(username: String)
    fun findByUsername(username: String): TotpDetails
}
