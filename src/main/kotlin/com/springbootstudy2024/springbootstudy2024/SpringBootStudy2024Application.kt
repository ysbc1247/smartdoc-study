package com.springbootstudy2024.springbootstudy2024

import com.springbootstudy2024.springbootstudy2024.chapter2.configuration.DbConfiguration
import com.springbootstudy2024.springbootstudy2024.chapter2.configuration.DbConnectionConfiguration
import com.springbootstudy2024.springbootstudy2024.chapter4.dto.ReleaseItem
import com.springbootstudy2024.springbootstudy2024.chapter4.dto.ReleaseNote
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import java.time.LocalDate
import java.util.Properties

@SpringBootApplication
@ConfigurationPropertiesScan("com.springbootstudy2024.springbootstudy2024")
class SpringBootStudy2024Application: CommandLineRunner {

    override fun run(vararg args: String?) {
        // Interface 상속을 통해 구현된 CommandLineRunner
        logger.info("SpringBootStudy2024Application CommandLineRunner Executed!")
    }

    @Bean(name = ["releaseNotes"])
    fun loadReleaseNotes(): Collection<ReleaseNote> {
        val releaseNotes = linkedSetOf<ReleaseNote>()

        val releaseNote1 = ReleaseNote(
            "v1.2.1",
            LocalDate.of(2021, 12, 30),
            "a7d2ea3",
            setOf(
                ReleaseItem(
                    "SBIP-123",
                    "The name of the matching-strategy property is incorrect in the action message of the failure analysis for a PatternParseException #28839"
                ),
                ReleaseItem(
                    "SBIP-124",
                    "ErrorPageSecurityFilter prevents deployment to a Servlet 3.1 compatible container #28790"
                ),
            ),
            emptySet(),
        )

        val releaseNote2 = ReleaseNote(
            "v1.2.0",
            LocalDate.of(2021, 11, 20),
            "44047f3",
            setOf(ReleaseItem("SBIP-125", "Support both kebab-case and camelCase as Spring init CLI Options #28138")),
            setOf(ReleaseItem("SBIP-126", "Profiles added using @ActiveProfiles have different precedence #28724")),
        )


        releaseNotes.addAll(setOf(releaseNote1, releaseNote2))
        return releaseNotes
    }


}

val logger = LoggerFactory.getLogger(SpringBootStudy2024Application::class.java)
fun main(args: Array<String>) {

    val properties = Properties()
    // 예시 1.
    // spring.config.on-not-found를 ignore로 정의한다.
    // ConfigDataEnvironment에 Property들에 대해서 정의되어 있음.
    properties.setProperty("spring.config.on-not-found", "ignore")
    val context = runApplication<SpringBootStudy2024Application>(*args) {
        this.setDefaultProperties(properties)
    }

    val dbConfigurationBean = context.getBean(DbConfiguration::class.java)
    val dbConnectionConfigurationBean = context.getBean(DbConnectionConfiguration::class.java)

    logger.info(dbConfigurationBean.getData())
    logger.info(dbConnectionConfigurationBean.getData())
}
