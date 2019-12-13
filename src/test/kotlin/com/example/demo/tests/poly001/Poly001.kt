package com.example.demo.tests.poly001

import com.example.demo.config.Jackson
import com.example.demo.testutils.junit5.testFactory
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException
import com.fasterxml.jackson.module.kotlin.readValue
import org.amshove.kluent.`should be instance of`
import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import java.util.*

@SpringBootTest
class Poly001 {

    /**
     * you can not JSON.parse() as subtype, without the type tag :(
     */
    @TestFactory
    fun test_parse_as_subtype_without_type_tag_will_fail() = testFactory {
        test("JSON.parse() with type tag - should work") {
            val sourceJson = """
            {"@type":"ADto","myId":"405334a1-f2b1-492a-b782-dc921572e0d8","createdAt":"2019-12-12T10:52:23.372952Z","a":100}
        """.trimIndent()
            val aDto = JSON.readValue<ADto>(sourceJson)
            println(aDto)
        }

        test("JSON.parse() without type tag - should fail") {
            val sourceJson = """
            {"myId":"405334a1-f2b1-492a-b782-dc921572e0d8","createdAt":"2019-12-12T10:52:23.372952Z","a":100}
        """.trimIndent()
            assertThrows<InvalidTypeIdException> {
                val aDto = JSON.readValue<ADto>(sourceJson)
                println(aDto)
            }
        }
    }

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
            val aReloaded = JSON.readValue<ADto>(aJson)
                    .also {
                        println("aReloaded: $it")
                        it `should be instance of` FDto::class
                        it `should be instance of` ADto::class
                        it.createdAt shouldEqual a.createdAt
                        it.myId shouldEqual a.myId
                        it shouldEqual a
                    }

            val fReloaded: FDto = JSON.readValue<FDto>(aJson)
                    .also {
                        println("fReloaded: $it")
                        it `should be instance of` FDto::class
                        it `should be instance of` ADto::class
                        it.createdAt shouldEqual a.createdAt
                        it.myId shouldEqual a.myId
                        it shouldEqual a
                        it shouldEqual aReloaded
                    }

        }
        test("b - works with jackson 2.10.1") {
            val b = BDto(
                    myId = UUID.fromString("405334a1-f2b1-492a-b782-dc921572e0d8"),
                    createdAt = Instant.parse("2019-12-12T10:52:23.372952Z"),
                    b = true
            )
            val bJson = JSON.writeValueAsString(b)
            val bReloaded: FDto = JSON.readValue<FDto>(bJson)
                    .also {
                        println("bReloaded: $it")
                        it `should be instance of` FDto::class
                        it `should be instance of` BDto::class
                        it.createdAt shouldEqual b.createdAt
                        it.myId shouldEqual b.myId
                        it shouldEqual b
                    }

            val fReloaded: FDto = JSON.readValue<FDto>(bJson)
                    .also {
                        println("fReloaded: $it")
                        it `should be instance of` FDto::class
                        it `should be instance of` BDto::class
                        it.createdAt shouldEqual b.createdAt
                        it.myId shouldEqual b.myId

                        it shouldEqual b
                        it shouldEqual bReloaded
                    }

        }

    }


}

private val JSON = Jackson.defaultMapper()

//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include= JsonTypeInfo.As.PROPERTY, property="myType")
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
