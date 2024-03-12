package com.springbootstudy2024.springbootstudy2024.chapter4.infocontributor

import org.springframework.boot.actuate.info.Info
import org.springframework.boot.actuate.info.InfoContributor
import org.springframework.stereotype.Component

@Component
class CourseInfoContributor() : InfoContributor {

    override fun contribute(builder: Info.Builder) {
        val exampleList = mutableListOf<ExampleData>()
        exampleList.add(ExampleData("hihi1", 1231123))
        exampleList.add(ExampleData("hihi2", 321321))
        builder.withDetail("example", exampleList)
    }

    data class ExampleData(val name: String, val rating: Int)
}
