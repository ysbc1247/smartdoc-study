package com.springbootstudy2024.springbootstudy2024.chapter6.service

import com.springbootstudy2024.springbootstudy2024.chapter6.model.TotpDetails
import com.springbootstudy2024.springbootstudy2024.chapter6.repository.TotpRepository
import com.springbootstudy2024.springbootstudy2024.chapter6.repository.UserRepository
import com.warrenstrange.googleauth.GoogleAuthenticator
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator
import org.apache.coyote.BadRequestException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TotpService(
    private val totpRepository: TotpRepository,
    private val userRepository: UserRepository,
) {
    private val googleAuth = GoogleAuthenticator()
   @Transactional
   fun generateAuthenticationQrUrl(
       username: String
   ): String? {
       val authKey = googleAuth.createCredentials()
       val secret = authKey.key
       totpRepository.deleteByUsername(username)
       totpRepository.save(TotpDetails(username, secret))
       return GoogleAuthenticatorQRGenerator.getOtpAuthURL(ISSUER, username, authKey)
   }

    fun isTotpEnabled(username: String): Boolean {
        return userRepository.findByUsername(username)?.isTotpEnabled ?: false
    }

    fun enableTotpForUser(
        username: String,
        code: Int
    ) {
        if(!verifyCode(username, code)) {
            throw BadRequestException()
        }
        val user = userRepository.findByUsername(username)
        user?.let {
            it.isTotpEnabled = true
            userRepository.save(it)
        }
    }

    fun verifyCode(
        username: String,
        code: Int
    ): Boolean {
        val totpDetails = totpRepository.findByUsername(username)
        return googleAuth.authorize(totpDetails.secret, code)
    }


    companion object {
        const val ISSUER = "CourseTracker"
    }
}
