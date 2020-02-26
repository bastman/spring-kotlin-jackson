package com.example.demo.examples.poly001

import com.example.demo.config.Jackson
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.readValue
import org.amshove.kluent.`should be instance of`
import org.amshove.kluent.`should not be instance of`
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class PolymorphicTests001 {


    @Test
    fun unknown() {
        val txt = """
            
            {
              "eventId": "2228400b-995d-41cb-9c28-fa7bc63c88a7",
              "eventTime": "2020-02-26T09:33:28.775183Z",
              "eventType": "the-event-A",
              "data": {
                "a": "bar"
              }
            }
            
               """.trimIndent()

        // decode as Unknown
        val decoded1: Unknown = JSON.readValue(txt)
        println(decoded1)
        println(decoded1.eventType)
        decoded1 `should be instance of` IEvent::class
        decoded1 `should not be instance of` IEventPolymorphic::class
        decoded1 `should be instance of` Unknown::class

        // decode as IEventPolymorphic (EventA)
        val decoded2: IEventPolymorphic = JSON.readValue(txt)
        println(decoded2)
        println(decoded2.eventType)
        decoded2 `should be instance of` IEvent::class
        decoded2 `should be instance of` IEventPolymorphic::class
        decoded2 `should be instance of` EventA::class

        // decoded1 (Simple) convertTo Event (RequestA)
        val decoded3: IEventPolymorphic = JSON.convertValue(decoded1)
        println(decoded3)
        println(decoded3.eventType)
        decoded3 `should be instance of` IEvent::class
        decoded3 `should be instance of` IEventPolymorphic::class
        decoded3 `should be instance of` EventA::class

        val r = when (val it = decoded3) {
            is EventA -> it
            is EventB -> error("should not happen")
            else -> error("should not happen")
        }
        println(r)
    }

    @Test
    fun requestA() {
        val txt = """
            
            {
              "eventId": "2228400b-995d-41cb-9c28-fa7bc63c88a7",
              "eventTime": "2020-02-26T09:33:28.775183Z",
              "eventType": "the-event-A",
              "data": {
                "a": "bar"
              }
            }
            
               """.trimIndent()

        // decode as Unknown
        val decoded1: Unknown = JSON.readValue(txt)
        println(decoded1)
        println(decoded1.eventType)
        decoded1 `should be instance of` IEvent::class
        decoded1 `should not be instance of` IEventPolymorphic::class
        decoded1 `should be instance of` Unknown::class

        // decode as IEventPolymorphic (EventA)
        val decoded2: IEventPolymorphic = JSON.readValue(txt)
        println(decoded2)
        println(decoded2.eventType)
        decoded2 `should be instance of` IEvent::class
        decoded2 `should be instance of` IEventPolymorphic::class
        decoded2 `should be instance of` EventA::class

        // decoded1 (Unknown) convertTo Event (RequestA)
        val decoded3: IEventPolymorphic = JSON.convertValue(decoded1)
        println(decoded3)
        println(decoded3.eventType)
        decoded3 `should be instance of` IEvent::class
        decoded3 `should be instance of` IEventPolymorphic::class
        decoded3 `should be instance of` EventA::class

        val r = when (val it = decoded3) {
            is EventA -> it
            is EventB -> error("should not happen")
            else -> error("should not happen")
        }

        // convert  Event (RequestA) -> Unknown
        val decoded4: Unknown = JSON.convertValue(decoded3)
        println(decoded4)
        println(decoded4.eventType)
        decoded4 `should be instance of` Unknown::class
    }

    @Test
    fun requestB() {
        val txt = """
            
            {
              "eventId": "2228400b-995d-41cb-9c28-fa7bc63c88a7",
              "eventTime": "2020-02-26T09:33:28.775183Z",
              "eventType": "the-event-B",
              "data": {
                "b": "bar"
              }
            }
            
               """.trimIndent()

        // decode as Unknown
        val decoded1: Unknown = JSON.readValue(txt)
        println(decoded1)
        println(decoded1.eventType)
        decoded1 `should be instance of` IEvent::class
        decoded1 `should not be instance of` IEventPolymorphic::class
        decoded1 `should be instance of` Unknown::class
        decoded1 `should not be instance of` EventA::class
        decoded1 `should not be instance of` EventB::class

        // decode as IEventPolymorphic (EventB)
        val decoded2: IEventPolymorphic = JSON.readValue(txt)
        println(decoded2)
        println(decoded2.eventType)
        decoded2 `should be instance of` IEvent::class
        decoded2 `should be instance of` IEventPolymorphic::class
        decoded2 `should be instance of` EventB::class
        decoded2 `should not be instance of` EventA::class
        decoded2 `should not be instance of` Unknown::class

        // decoded1 (Unknown) convertTo Event (RequestB)
        val decoded3: IEventPolymorphic = JSON.convertValue(decoded1)
        println(decoded3)
        println(decoded3.eventType)
        decoded3 `should be instance of` IEvent::class
        decoded3 `should be instance of` IEventPolymorphic::class
        decoded3 `should be instance of` EventB::class
        decoded3 `should not be instance of` EventA::class
        decoded3 `should not be instance of` Unknown::class
    }


}

private val JSON = Jackson.defaultMapper()
