package com.springbootstudy2024.springbootstudy2024.chapter6.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "CT_TOTP_DETAILS")
class TotpDetails(
    val username: String,
    val secret: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}
