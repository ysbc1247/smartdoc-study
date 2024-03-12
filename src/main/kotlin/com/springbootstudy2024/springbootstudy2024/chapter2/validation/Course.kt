package com.springbootstudy2024.springbootstudy2024.chapter2.validation

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size

class Course(
    val id: Long,
    val name: String,
    val category: String,

    @field:Min(value = 1, message = "평점은 1 이상이어야 합니다.")
    @field:Max(value = 5, message = "평점은 5 이하여야 합니다.")
    // 대체 가능: @Range(min = 1, max = 5, message = "평점은 1 이상 5 이하여야 합니다.")
    val rating: Int,

    @field:Size(min = 5, max = 300, message = "강의 정보는 최소 5자, 최대 300자까지 입력 가능합니다.")
    val description: String,

    @EvenNumber(message = "정원은 짝수여야 합니다.")
    @field:Max(value = 100, message = "정원은 100 이하여야 합니다.")
    val studentCapacity: Int,
)
