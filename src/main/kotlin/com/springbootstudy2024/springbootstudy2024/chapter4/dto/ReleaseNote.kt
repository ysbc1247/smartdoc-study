package com.springbootstudy2024.springbootstudy2024.chapter4.dto

import java.time.LocalDate

data class ReleaseNote(
    val version: String,
    val releaseDate: LocalDate,
    val commitTag: String,
    val newReleases: Set<ReleaseItem>,
    val bugFixes: Set<ReleaseItem>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ReleaseNote) return false
        return version == other.version
    }

    override fun hashCode(): Int {
        return version?.hashCode() ?: 0
    }
}
