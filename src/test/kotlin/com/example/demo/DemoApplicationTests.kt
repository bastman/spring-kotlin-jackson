package com.example.demo

import com.example.demo.config.Jackson
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.Duration
import java.time.Instant

@SpringBootTest
class DemoApplicationTests {
    @Test
    fun contextLoads() {
    }
    @Test
    fun a() {
        val a = A(int = 101, bool = false, string = "xxx", double = 1.3,
                instant = Instant.now(), duration = Duration.ofHours(3)

        )
        val json = JSON.writeValueAsString(a)
        println(json)
        val aReloaded:A = JSON.readValue(json)
        println(JSON.writeValueAsString(aReloaded))
                /*
                .also {
                    println(JSON.writeValueAsString(it))
                }

                 */

        val aConverted:A = JSON.convertValue(aReloaded)
        println(JSON.writeValueAsString(aConverted))
    }

    @Test
    fun polymorphic() {
        // see: https://github.com/FasterXML/jackson-module-kotlin/commit/d5d76529f538d69fb71002b3bb593fa558e40b55
        val items = listOf(
                Poly.X(int=100), Poly.Y(string = "yyy")
        )
        val json = JSON.writeValueAsString(items)
        println(json)
        val aReloaded:List<Poly> = JSON.readValue(json)
        println(JSON.writeValueAsString(aReloaded))
    }

    @Test
    fun polymorphic_customized() {

        // bug or feature?
        // polymorphic works with arrayOf(Github239EitherCustomized)
        // but does not work with listOf(Github239EitherCustomized)

        // see: https://github.com/FasterXML/jackson-module-kotlin/commit/d5d76529f538d69fb71002b3bb593fa558e40b55
        val items = arrayOf(
                Github239EitherCustomized.A(), Github239EitherCustomized.B()
        )
        val json = JSON.writeValueAsString(items)
        println(json)
        //val aReloaded:List<Github239EitherCustomized> = JSON.readValue(json)
        //println(JSON.writeValueAsString(aReloaded))
    }

    @Test
    fun poly_TestGithub239_001() {
        val json = """[
        {
            "@type": "a",
            "field": "value"
        },
        {
            "@type": "b",
            "otherField": "1234"
        }
    ]"""

        val array = JSON.readValue<Array<Github239Either>>(json)
        println(JSON.writeValueAsString(array))
        assertEquals(2, array.size)
        assertEquals(Github239Either.A("value"), array[0])
        assertEquals(Github239Either.B("1234"), array[1])
    }

    @Test
    fun poly_TestGithub239_002() {
        val json = """[
        {
            "@type": "a",
            "field": "value"
        },
        {
            "@type": "b",
            "otherField": "1234"
        }
    ]"""
        val array = JSON.readValue<Array<Github239EitherCustomized>>(json)

        println(JSON.writeValueAsString(array))
        assertEquals(2, array.size)
        assertEquals(Github239EitherCustomized.A("value"), array[0])
        assertEquals(Github239EitherCustomized.B("1234"), array[1])
    }
}
private val JSON = Jackson.defaultMapper()


data class A(
        val int:Int,
        val bool:Boolean,
        val string:String,
        val double:Double,
        val instant:Instant,
        val duration:Duration
)

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
sealed class Poly {
    // see: https://github.com/FasterXML/jackson-module-kotlin/commit/d5d76529f538d69fb71002b3bb593fa558e40b55

    enum class PolyType {
        x, y
    }

    @JsonTypeName("a")
    data class X(val int:Int):Poly()
    @JsonTypeName("b")
    data class Y(val string:String):Poly()
}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes(
        JsonSubTypes.Type(Github239EitherCustomized.A::class, name = "a"),
        JsonSubTypes.Type(Github239EitherCustomized.B::class, name = "b")
)
sealed class Github239EitherCustomized {

    data class A(var field: String = "") : Github239EitherCustomized()

    data class B(var otherField: String = "") : Github239EitherCustomized()

}


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
sealed class Github239Either {

    @JsonTypeName("a")
    data class A(var field: String = "") : Github239Either()

    @JsonTypeName("b")
    data class B(var otherField: String = "") : Github239Either()

}
