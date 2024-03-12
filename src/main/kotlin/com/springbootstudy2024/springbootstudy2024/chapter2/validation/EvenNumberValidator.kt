package com.springbootstudy2024.springbootstudy2024.chapter2.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class EvenNumberValidator: ConstraintValidator<EvenNumber, Int> {

    override fun isValid(value: Int, context: ConstraintValidatorContext): Boolean {
        return value % 2 == 0
    }
}
