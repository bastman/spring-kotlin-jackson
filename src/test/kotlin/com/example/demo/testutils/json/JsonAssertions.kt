package com.example.demo.testutils.json

import com.example.demo.config.Jackson
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.amshove.kluent.shouldEqual

fun defaultJsonMapper(): ObjectMapper = Jackson.defaultMapper()

fun simpleJsonMapper(): ObjectMapper = jacksonObjectMapper()
        .registerModules(
                JavaTimeModule(),
                Jdk8Module()
        )
        .disable(
                SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
                SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS,
                SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS,
                SerializationFeature.WRITE_ENUMS_USING_INDEX
        )

fun ObjectMapper.stringify(value: Any?): String = writeValueAsString(value)
fun ObjectMapper.withSerializationFeature(feature: SerializationFeature, state: Boolean): ObjectMapper = copy()
        .apply { configure(feature, state) }

fun ObjectMapper.withSerializationFeatureOrderMapEntriesByKeys(enabled: Boolean = true): ObjectMapper =
        withSerializationFeature(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, enabled)

fun String.toNormalizedJson(objectMapper: ObjectMapper = simpleJsonMapper()): String {
    val mapper = objectMapper
            .withSerializationFeatureOrderMapEntriesByKeys(true)
    val decoded: Any? = mapper.readValue(this)// mapper.readTree(this)

    return mapper
            .writeValueAsString(decoded)
            .trim()
}

infix fun String.shouldEqualJson(theOther: String) =
        this.toNormalizedJson() shouldEqual theOther.toNormalizedJson()

fun Any?.toJson(mapper: ObjectMapper = defaultJsonMapper()): String = mapper.writeValueAsString(this)

