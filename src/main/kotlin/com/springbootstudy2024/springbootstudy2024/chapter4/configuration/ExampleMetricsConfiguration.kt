package com.springbootstudy2024.springbootstudy2024.chapter4.configuration

import com.springbootstudy2024.springbootstudy2024.chapter4.service.ExampleService
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.DistributionSummary
import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ExampleMetricsConfiguration {
    @Bean
    fun createCounter(meterRegistry: MeterRegistry): Counter {
        return Counter.builder("example.counter")
            .description("A simple counter")
            .tags("example", "counter")
            .register(meterRegistry)
    }

    @Bean
    fun createGauges(meterRegistry: MeterRegistry, exampleService: ExampleService): Gauge {
        return Gauge.builder("example.gauges", exampleService.getCount()::toDouble)
            .description("A simple gauges")
            .tags("example", "gauges")
            .register(meterRegistry)
    }

    @Bean
    fun createTimer(meterRegistry: MeterRegistry): Timer {
        return Timer.builder("example.timer")
            .description("A simple timer")
            .tags("example", "timer")
            .register(meterRegistry)
    }


    @Bean
    fun createDistributionSummary(meterRegistry: MeterRegistry): DistributionSummary {
        return DistributionSummary.builder("example.distributionSummary")
            .description("A simple distributionSummary")
            .tags("example", "distributionSummary")
            .register(meterRegistry)
    }
}



