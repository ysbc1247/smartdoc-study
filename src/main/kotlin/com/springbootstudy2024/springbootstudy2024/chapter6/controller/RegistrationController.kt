package com.springbootstudy2024.springbootstudy2024.chapter6.controller

import com.springbootstudy2024.springbootstudy2024.chapter6.dto.UserDto
import com.springbootstudy2024.springbootstudy2024.chapter6.service.GoogleRecaptchaService
import com.springbootstudy2024.springbootstudy2024.chapter6.service.UserService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping

@Controller
class RegistrationController(
    private val userService: UserService,
    private val googleRecaptchaService: GoogleRecaptchaService,
) {
    @GetMapping("/adduser")
    fun register(
        @ModelAttribute("user")
        user: UserDto = UserDto()
    ): String {
        return "add-user"
    }

    @PostMapping("/adduser")
    fun loginError(
        @ModelAttribute("user")
        userDto: UserDto,
        result: BindingResult,
        httpServletRequest: HttpServletRequest,
    ): String {
        if (result.hasErrors())
            return "add-user"

        val response = httpServletRequest.getParameter("g-recaptcha-response")
        if(response == null)
            return "add-user"
        val ip = httpServletRequest.remoteAddr
        val responseDto = googleRecaptchaService.verify(
            ip,
            response
        )
        if(!responseDto.success)
            return "redirect:adduser?incorrectCaptcha"

        userService.createUser(userDto)
        return "redirect:adduser?success"
    }
}
