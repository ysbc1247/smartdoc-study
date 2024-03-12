package com.springbootstudy2024.springbootstudy2024.chapter6.controller

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class LoginController {
    @GetMapping("/index")
    fun index(): String? {
        val a = SecurityContextHolder.getContext().authentication
        return "index"
    }

    @GetMapping("/login")
    fun login(): String {
        return "login"
    }

    @GetMapping("/login-error")
    fun loginError(
        model: Model
    ): String {
        model.addAttribute("loginError", true)

        return "login"
    }

    @GetMapping("/login-locked")
    fun loginLocked(
        model: Model
    ): String {
        model.addAttribute("loginLocked", true)
        return "login"
    }
}
