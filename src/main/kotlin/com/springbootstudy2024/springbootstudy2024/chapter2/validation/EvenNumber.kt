package com.springbootstudy2024.springbootstudy2024.chapter2.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [EvenNumberValidator::class])
annotation class EvenNumber(
    val message: String = "홀수는 입력할 수 없습니다.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
)
