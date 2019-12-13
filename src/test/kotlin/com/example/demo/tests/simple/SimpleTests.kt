package com.example.demo.tests.simple

import com.example.demo.config.Jackson
import com.example.demo.testutils.json.shouldEqualJson
import com.example.demo.testutils.json.stringify
import com.example.demo.testutils.junit5.testFactory
import com.example.demo.testutils.random.*
import com.example.demo.testutils.resources.CodeSourceResources
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
    private fun baseName(index: Int) = "$index".padStart(5, "0".toCharArray().first())

    private fun resourceSinkFolderLocation(): String = CodeSourceResources
            .fileLocationAsString()
            .let {
                CodeSourceResources.replaceLocationSuffix(
                        location = it,
                        oldSuffix = "/out/test/classes/",
                        newSuffix = "/src/test/resources",
                        oldSuffixRequired = true
                )
            }

    @Test
    fun contextLoads() {
    }

    @Test
    fun generate_A() {
        val saveToFilesystem = false
        val startIndex = 0
        val maxItems = 100
        val sinkFolder = resourceSinkFolderLocation()
        val sources = (0..maxItems).map {
            A(
                    int = (Int.MIN_VALUE..Int.MAX_VALUE).random(),
                    bool = randomBoolean(),
                    string = randomString(prefix = "random-string-"),
                    double = (Double.MIN_VALUE..Double.MAX_VALUE).random(),
                    instant = listOf(
                            Instant.now(),
                            ((Instant.parse("1719-12-12T08:19:42.651828Z"))..(Instant.parse("2319-12-12T08:19:42.651828Z"))).randomInstant()
                    )
                            .shuffled().first(),
                    duration = (Int.MIN_VALUE..Int.MAX_VALUE).randomDurationOfSeconds(),
                    uuid = UUID.randomUUID()
            )
        }


        sources.forEachIndexed { index, testCase ->
            val baseName = baseName(index = startIndex + index)
            val resource = resourceName(baseName)
            val sink = "$sinkFolder$resource"
            val json = JSON.stringify(testCase)

            println("===== testcase (json): $resource ====")
            println(json)
            println("====== $sink ===========")
            if (saveToFilesystem) {
                CodeSourceResources.writeTextFile(
                        location = sink, content = json
                )
            }
        }


    }


    @TestFactory
    fun test_A() = testFactory {
        (0..100).forEach { tcIndex ->
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

