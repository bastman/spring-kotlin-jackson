package com.example.demo.tests.poly001

import com.example.demo.config.Jackson
import com.example.demo.testutils.junit5.testFactory
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import com.fasterxml.jackson.module.kotlin.readValue
import org.amshove.kluent.`should be instance of`
import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.TestFactory
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import java.util.*

@SpringBootTest
class Poly001 {

    @TestFactory
    fun foo() = testFactory {
        test("a - works with jackson 2.10.1") {
            val a = ADto(
                    myId = UUID.fromString("405334a1-f2b1-492a-b782-dc921572e0d8"),
                    createdAt = Instant.parse("2019-12-12T10:52:23.372952Z"),
                    a = 100
            )
            val aJson = JSON.writeValueAsString(a)
            println("json: $aJson")
            val aReloaded: ADto = JSON.readValue(aJson)
            aReloaded `should be instance of` FDto::class
            aReloaded `should be instance of` ADto::class
            aReloaded.createdAt shouldEqual a.createdAt
            aReloaded.myId shouldEqual a.myId
            aReloaded shouldEqual a

            val fReloaded: FDto = JSON.readValue(aJson)
            fReloaded `should be instance of` FDto::class
            fReloaded `should be instance of` ADto::class
            fReloaded.createdAt shouldEqual a.createdAt
            fReloaded.myId shouldEqual a.myId
            fReloaded shouldEqual a
            fReloaded shouldEqual aReloaded
        }
        test("b - works with jackson 2.10.1") {
            val b = BDto(
                    myId = UUID.fromString("405334a1-f2b1-492a-b782-dc921572e0d8"),
                    createdAt = Instant.parse("2019-12-12T10:52:23.372952Z"),
                    b = true
            )
            val bJson = JSON.writeValueAsString(b)
            val bReloaded: FDto = JSON.readValue(bJson)
            bReloaded `should be instance of` FDto::class
            bReloaded `should be instance of` BDto::class
            bReloaded.createdAt shouldEqual b.createdAt
            bReloaded.myId shouldEqual b.myId
            bReloaded shouldEqual b

            val fReloaded: FDto = JSON.readValue(bJson)
            fReloaded `should be instance of` FDto::class
            fReloaded `should be instance of` BDto::class
            fReloaded.createdAt shouldEqual b.createdAt
            fReloaded.myId shouldEqual b.myId

            fReloaded shouldEqual b
            fReloaded shouldEqual bReloaded
        }

    }

}

private val JSON = Jackson.defaultMapper()

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
sealed class FDto {
    abstract val myId: UUID
    abstract val createdAt: Instant
}

@JsonTypeName(ADto.JSON_TYPE)
data class ADto(
        override val myId: UUID,
        override val createdAt: Instant,
        val a: Int
) : FDto() {
    companion object {
        const val JSON_TYPE = "ADto"
    }
}

@JsonTypeName(BDto.JSON_TYPE)
data class BDto(
        override val myId: UUID,
        override val createdAt: Instant,
        val b: Boolean
) : FDto() {
    companion object {
        const val JSON_TYPE = "BDto"
    }
}
