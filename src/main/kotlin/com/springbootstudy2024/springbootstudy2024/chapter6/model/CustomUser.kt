package com.springbootstudy2024.springbootstudy2024.chapter6.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

data class CustomUser(
    @get:JvmName("customUsername")
    val username: String?,
    @get:JvmName("customPassword")
    val password: String?,
    val enabled: Boolean,
    val accountNonExpired: Boolean,
    val credentialsNonExpired: Boolean,
    val accountNonLocked: Boolean,
    @get:JvmName("customAuthorities")
    val authorities: Collection<GrantedAuthority?>?,
    var totpEnabled: Boolean,
): User(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities)
