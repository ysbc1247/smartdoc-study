package com.springbootstudy2024.springbootstudy2024.chapter6.service

import com.springbootstudy2024.springbootstudy2024.chapter6.dto.RecaptchaDto
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class GoogleRecaptchaService {
    private val VERIFY_URL =
        "https://www.google.com/recaptcha/api/siteverify" + "?secret={secret}&remoteip={remoteip}&response={response}"

    private val restTemplate = RestTemplate()

    private var secret: String = "6LdrdIEpAAAAAG8l2iCKTThCM90Ws1WPxjK5RUyH"

    fun verify(
        ip: String,
        recaptchaResponse: String
    ): RecaptchaDto {
        val request = HashMap<String, String>()
        request["remoteip"] = ip
        request["response"] = recaptchaResponse
        request["secret"] = secret

        val response = restTemplate.getForEntity(VERIFY_URL, Map::class.java, request)
        val body = response.body as Map<String, Any>
        val success = body["success"] as Boolean

        var errors: List<String> = ArrayList()

        if (!success) {
            errors = body["error-codes"] as List<String>
        }

        return RecaptchaDto(success, errors)
    }
}
