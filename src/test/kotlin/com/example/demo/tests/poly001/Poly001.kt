package com.example.demo.tests.poly001

import com.example.demo.config.Jackson
import com.example.demo.testutils.junit5.testFactory
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import com.fasterxml.jackson.module.kotlin.readValue
import org.amshove.kluent.`should be instance of`
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import java.util.*

@SpringBootTest
class Poly001 {

    @TestFactory
    fun foo() = testFactory {
        test("a") {
            val a=ADto(fId = UUID.randomUUID(), createdAt = Instant.now(), a=100)
            val aJson = JSON.writeValueAsString(a)
            println("json: $aJson")
            val aReloaded:ADto = JSON.readValue(aJson)
            aReloaded `should be instance of` FDto::class
            aReloaded `should be instance of` ADto::class
        }
        test("b") {
            val b=BDto(fId = UUID.randomUUID(), createdAt = Instant.now(), b=true)
            val bJson = JSON.writeValueAsString(b)
            val bReloaded:FDto = JSON.readValue(bJson)
            bReloaded `should be instance of` FDto::class
            bReloaded `should be instance of` ADto::class
        }

    }

}

private val JSON = Jackson.defaultMapper()

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
sealed class FDto {
    abstract val fId: UUID
    abstract val createdAt: Instant
}

@JsonTypeName(ADto.JSON_TYPE)
data class ADto(
        override val fId: UUID,
        override val createdAt: Instant,
        val a:Int
):FDto() {
    companion object {
        const val JSON_TYPE="ADto"
    }
}

@JsonTypeName(BDto.JSON_TYPE)
data class BDto(
        override val fId: UUID,
        override val createdAt: Instant,
        val b:Boolean
):FDto() {
    companion object {
        const val JSON_TYPE="BDto"
    }
}
