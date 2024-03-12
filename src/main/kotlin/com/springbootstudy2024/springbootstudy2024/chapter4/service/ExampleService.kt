package com.springbootstudy2024.springbootstudy2024.chapter4.service

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.DistributionSummary
import io.micrometer.core.instrument.Timer
import org.springframework.stereotype.Service
import java.util.concurrent.Callable
import kotlin.random.Random

@Service
class ExampleService(private val counter: Counter,private val timer: Timer, private val distributionSummary: DistributionSummary) {
    fun incrementCounter() {
        counter.increment()
    }

    fun getCount(): Double {
        return 1.0
    }


    fun createTimer(): String {
        distributionSummary.record(Random.nextDouble())
        return timer.recordCallable(Callable {
            Thread.sleep(1000)
            "hello"
        })?: "error"
    }

}
