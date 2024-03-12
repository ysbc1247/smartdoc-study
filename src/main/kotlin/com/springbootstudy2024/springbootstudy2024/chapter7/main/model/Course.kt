package com.springbootstudy2024.springbootstudy2024.chapter7.main.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "COURSES")
data class Course(

    @Column(name = "NAME")
    var name: String? = null,

    @Column(name = "CATEGORY")
    var category: String? = null,

    @Column(name = "RATING")
    var rating: Int? = null,

    @Column(name = "DESCRIPTION")
    var description: String? = null,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long? = null
}
