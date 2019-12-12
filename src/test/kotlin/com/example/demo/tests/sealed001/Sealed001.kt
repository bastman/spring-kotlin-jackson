package com.example.demo.tests.sealed001

import com.example.demo.config.Jackson
import com.example.demo.testutils.junit5.testFactory
import com.fasterxml.jackson.module.kotlin.readValue
import org.amshove.kluent.`should be instance of`
import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.TestFactory
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import java.util.*

@SpringBootTest
class Sealed001 {

    @TestFactory
    fun foo() = testFactory {
        test("a (works with jackson 2.9.9") {
            val a = ADto(
                    createdAt = Instant.parse("2019-12-12T10:52:23.372952Z"),
                    myId = UUID.fromString("405334a1-f2b1-492a-b782-dc921572e0d8"),
                    a = 100
            )
            val aJson = JSON.writeValueAsString(a)
            println("json: $aJson")
            val aReloaded: ADto = JSON.readValue(aJson)
            println("json: ${JSON.writeValueAsString(aReloaded)}")
            aReloaded `should be instance of` FDto::class
            aReloaded `should be instance of` ADto::class
            aReloaded.createdAt shouldEqual a.createdAt
            aReloaded.myId shouldEqual a.myId
        }
        test("b") {
            val b = BDto(createdAt = Instant.now(), myId = UUID.randomUUID(),b = true)
            val bJson = JSON.writeValueAsString(b)
            val bReloaded: FDto = JSON.readValue(bJson)
            bReloaded `should be instance of` FDto::class
            bReloaded `should be instance of` ADto::class
        }

    }


}


private val JSON = Jackson.defaultMapper()

sealed class FDto {
    abstract val createdAt: Instant
    abstract val myId:UUID
}

data class ADto(
        override val createdAt: Instant,
        override val myId: UUID,
        val a: Int
) : FDto()

data class BDto(
        override val createdAt: Instant,
        override val myId: UUID,
        val b: Boolean
) : FDto()
