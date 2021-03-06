package com.example.demo.config

import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import mu.KLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Jackson {

    @Bean
    fun objectMapper(): ObjectMapper = defaultMapper()
            .also {
                logger.info {
                    """
                      jackson modules:  ${it.registeredModuleIds}
                    """.trimIndent()
                }
            }

    companion object : KLogging() {
        fun defaultMapper(): ObjectMapper = jacksonObjectMapper()
                .findAndRegisterModules()
                // toJson()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
                // fromJson()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)
                .disable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                .disable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                .enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
                .enable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS)
                .withStrictNullsEnabled()
    }

}

/**
 * enable strict null checks for List<T?>
 * fail on [null]
 * otherwise you need to ... //  @get:JsonSetter(contentNulls = Nulls.FAIL)
 */
private fun ObjectMapper.withStrictNullsEnabled():ObjectMapper = setDefaultSetterInfo(JsonSetter.Value.forContentNulls(Nulls.FAIL))

