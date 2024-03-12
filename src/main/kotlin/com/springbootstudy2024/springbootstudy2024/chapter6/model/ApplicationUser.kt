package com.springbootstudy2024.springbootstudy2024.chapter6.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "CT_USERS")
class ApplicationUser(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    var firstName: String?,
    var lastName: String?,
    var username: String?,
    var email: String?,
    var password: String?,
    var isTotpEnabled: Boolean,
    var authorities: List<String>,
)
