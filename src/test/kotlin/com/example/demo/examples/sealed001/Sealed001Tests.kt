package com.example.demo.examples.sealed001

import com.example.demo.config.Jackson
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.readValue
import org.amshove.kluent.`should be instance of`
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class Sealed001Tests {

    @Test
    fun simple() {
        val txt = """
            
            {
              "eventId": "2228400b-995d-41cb-9c28-fa7bc63c88a7",
              "eventTime": "2020-02-26T09:33:28.775183Z",
              "eventType": "events:RequestA",
              "data": {
                "a": "bar"
              }
            }
            
               """.trimIndent()

        // decode as Simple
        val decoded1: Simple = JSON.readValue(txt)
        println(decoded1)
        println(decoded1.eventType)
        decoded1 `should be instance of` Simple::class

        // decode as Event (RequestA)
        val decoded2: Event = JSON.readValue(txt)
        println(decoded2)
        println(decoded2.eventType)
        decoded2 `should be instance of` Event::class
        decoded2 `should be instance of` RequestA::class

        // decoded1 (Simple) convertTo Event (RequestA)
        val decoded3: Event = JSON.convertValue(decoded1)
        println(decoded3)
        println(decoded3.eventType)
        decoded3 `should be instance of` Event::class
        decoded3 `should be instance of` RequestA::class

        val r = when (val it = decoded3) {
            is RequestA -> it
            is RequestB -> error("should not happen")
        }
        println(r)
    }


    @Test
    fun requestA() {
        val txt = """
            
            {
              "eventId": "2228400b-995d-41cb-9c28-fa7bc63c88a7",
              "eventTime": "2020-02-26T09:33:28.775183Z",
              "eventType": "events:RequestA",
              "data": {
                "a": "bar"
              }
            }
            
               """.trimIndent()

        // decode as Simple
        val decoded1: Simple = JSON.readValue(txt)
        println(decoded1)
        println(decoded1.eventType)
        decoded1 `should be instance of` Simple::class

        // decode as Event (RequestA)
        val decoded2: Event = JSON.readValue(txt)
        println(decoded2)
        println(decoded2.eventType)
        decoded2 `should be instance of` Event::class
        decoded2 `should be instance of` RequestA::class

        // decoded1 (Simple) convertTo Event (RequestA)
        val decoded3: Event = JSON.convertValue(decoded1)
        println(decoded3)
        println(decoded3.eventType)
        decoded3 `should be instance of` Event::class
        decoded3 `should be instance of` RequestA::class

        val r = when (val it = decoded3) {
            is RequestA -> it
            is RequestB -> error("should not happen")
        }
        println(r)

        // convert  Event (RequestA) -> Simple
        val decoded4: Simple = JSON.convertValue(decoded3)
        println(decoded3)
        println(decoded3.eventType)
        decoded4 `should be instance of` Simple::class
    }

    @Test
    fun requestB() {
        val txt = """
            
            {
              "eventId": "2228400b-995d-41cb-9c28-fa7bc63c88a7",
              "eventTime": "2020-02-26T09:33:28.775183Z",
              "eventType": "events:RequestB",
              "data": {
                "b": "bar"
              }
            }
            
               """.trimIndent()

        // decode as Simple
        val decoded1: Simple = JSON.readValue(txt)
        println(decoded1)
        println(decoded1.eventType)
        decoded1 `should be instance of` Simple::class

        // decode as Event (RequestB)
        val decoded2: Event = JSON.readValue(txt)
        println(decoded2)
        println(decoded2.eventType)
        decoded2 `should be instance of` Event::class
        decoded2 `should be instance of` RequestB::class

        // decoded1 (Simple) convertTo Event (RequestB)
        val decoded3: Event = JSON.convertValue(decoded1)
        println(decoded3)
        println(decoded3.eventType)
        decoded3 `should be instance of` Event::class
        decoded3 `should be instance of` RequestB::class

        val r = when (val it = decoded3) {
            is RequestA -> error("should not happen")
            is RequestB -> it
        }
        println(r)
    }
}


private val JSON = Jackson.defaultMapper()

