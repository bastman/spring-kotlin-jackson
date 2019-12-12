package com.example.demo.tests.simple

import com.example.demo.config.Jackson
import com.example.demo.testutils.json.shouldEqualJson
import com.example.demo.testutils.json.stringify
import com.example.demo.testutils.junit5.testFactory
import com.example.demo.util.resources.loadResource
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.readValue
import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.springframework.boot.test.context.SpringBootTest
import java.time.Duration
import java.time.Instant
import java.util.*

@SpringBootTest
class SimpleTests {

    private val resourceFolder = "golden-test-data/simple"
    private fun resourceName(baseName: String) = "/$resourceFolder/test-$baseName.json"
    private fun baseName(index: Int) = "$index".padStart(4, "0".toCharArray().first())

    @Test
    fun contextLoads() {
    }

    @Test
    fun generate_A() {
        val sources = listOf(
                A(
                        int = 101, bool = false, string = "xxx", double = 1.3,
                        instant = Instant.now(), duration = Duration.ofHours(3),
                        uuid = UUID.randomUUID()
                ),
                A(
                        int = 102, bool = true, string = "yyy", double = 1.312345677,
                        instant = Instant.now(), duration = Duration.ofMinutes(2),
                        uuid = UUID.randomUUID()
                )
        )

        val startIndex = 0
        sources.forEachIndexed { index, testCase ->
            val baseName = baseName(index = startIndex + index)
            val resource = resourceName(baseName)
            val json = JSON.stringify(testCase)

            println("===== testcase (json): $resource ====")
            println(json)
            println("====== $resource ===========")
        }


    }


    @TestFactory
    fun test_A() = testFactory {
        (0..1).forEach { tcIndex ->
            val baseName = baseName(tcIndex)
            val resourceName = resourceName(baseName)
            test(name = "test: $resourceName") {
                val tcLoadedTxt: String = loadResource(resourceName)
                        .also { println("tc.loaded.txt: $it") }
                val tcLoaded: A = JSON.readValue(tcLoadedTxt)
                println("tc.loaded: $tcLoaded")

                val tcLoadedToJson: String = JSON.writeValueAsString(tcLoaded)
                        .also {
                            val given = it
                            val expected = tcLoadedTxt
                            println("given      : tc.loaded.toJson  : $given")
                            println("expected   : tc.loaded.txt     : $expected")
                            given shouldEqualJson expected
                        }

                val tcConverted: A = JSON.convertValue(tcLoaded)
                println("tc.converted: $tcConverted")
                val tcConvertedToJson = JSON.writeValueAsString(tcConverted)
                        .also {
                            val given = it
                            val expected = tcLoadedTxt
                            println("given      : tc.converted.toJson   : $given")
                            println("expected   : tc.loaded.txt         : $expected")
                            given shouldEqualJson expected
                        }

                Pair(tcConverted, tcLoaded).also {
                    val (given, expected) = it
                    println("given      : tc.converted  : $given")
                    println("expected   : tc.loaded     : $expected")
                    given shouldEqual expected
                }

            }
        }

    }


}

private val JSON = Jackson.defaultMapper()

data class A(
        val int: Int,
        val bool: Boolean,
        val string: String,
        val double: Double,
        val instant: Instant,
        val duration: Duration,
        val uuid: UUID
)

