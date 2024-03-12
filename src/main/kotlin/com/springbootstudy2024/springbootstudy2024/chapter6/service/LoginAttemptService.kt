package com.springbootstudy2024.springbootstudy2024.chapter6.service

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import org.springframework.stereotype.Service
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit

@Service
class LoginAttemptService {
    private val loginAttemptCache = CacheBuilder.newBuilder()
        .expireAfterWrite(1, TimeUnit.DAYS)
        .build(
            object: CacheLoader<String, Int>() {
                override fun load(key: String): Int {
                    return 0
                }
            },
        )

    fun loginSuccess(username: String) {
        loginAttemptCache.invalidate(username)
    }

    fun loginFailed(username: String) {
        var failedAttemptCounter = 0

        failedAttemptCounter = try {
            loginAttemptCache.get(username)
        } catch (e: ExecutionException) {
            0;
        }

        failedAttemptCounter++
        loginAttemptCache.put(username, failedAttemptCounter + 1)
    }

    fun isBlocked(username: String): Boolean {
        return try {
            loginAttemptCache[username] >= MAX_ATTEMPTS_COUNT
        } catch (e: ExecutionException) {
            false
        }
    }

    companion object {
        private const val MAX_ATTEMPTS_COUNT = 3
    }
}
