package com.springbootstudy2024.springbootstudy2024.chapter6.controller

import com.springbootstudy2024.springbootstudy2024.chapter6.model.CustomUser
import com.springbootstudy2024.springbootstudy2024.chapter6.model.TotpCode
import com.springbootstudy2024.springbootstudy2024.chapter6.service.TotpService
import org.apache.coyote.BadRequestException
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

@Controller
class AccountController(
    val totpService: TotpService
) {
    @GetMapping("/account")
    fun getAccount(
        model: Model,
        @AuthenticationPrincipal
        customUser: CustomUser?
    ): String {
        if (customUser != null && !customUser.totpEnabled ) {
            model.addAttribute("totpEnabled", false)
            model.addAttribute("configureTotp", true)
        }
        else {
            model.addAttribute("totpEnabled", true)
        }

        return "account"
    }

    @GetMapping("/setup-totp")
    fun getGoogleAuthenticatorQrUrl(
        model: Model,
        @AuthenticationPrincipal
        customUser: CustomUser
    ): String {
        val username = customUser.username!!
        val isTotp = customUser.totpEnabled

        if (!isTotp) {
            model.addAttribute("qrUrl", totpService.generateAuthenticationQrUrl(username))
            model.addAttribute("code", TotpCode())
            return "account"
        }

        model.addAttribute("totpEnabled", true)
        return "account"
    }

    @PostMapping("/confirm-totp")
    fun confirmGoogleAuthenticatorSetup(
        model: Model,
        @AuthenticationPrincipal
        customUser: CustomUser,
        totpCode: TotpCode,
    ): String {
        if(!customUser.totpEnabled) {
            try {
                totpService.enableTotpForUser(
                    customUser.username!!,
                    totpCode.code
                )
            } catch (e: BadRequestException) {
                model.addAttribute("totpEnabled", customUser.totpEnabled)
                model.addAttribute("confirmError", true)
                model.addAttribute("configureTotp", true)
                model.addAttribute("code", TotpCode())
                return "account"
            }

            model.addAttribute("totpEnabled", true)
        }

        customUser.totpEnabled = true
        return "redirect:/logout"
    }
}
