package com.example.demo.testutils.random

import java.math.BigDecimal
import java.time.Duration
import java.time.Instant
import java.util.*

fun randomBoolean(): Boolean = listOf(true, false).shuffled().first()
/*
fun ClosedRange<Int>.random() =
        Random().nextInt((endInclusive + 1) - start) + start
*/
fun ClosedRange<Int>.randomLong(): Long = (start.toLong() .. endInclusive.toLong()).random().toLong()


fun ClosedRange<Int>.randomDurationOfSeconds(): Duration = Duration.ofSeconds(randomLong())

// see: http://www.baeldung.com/java-generate-random-long-float-integer-double
fun ClosedRange<Double>.random(): Double {
    val leftLimit = start
    val rightLimit = endInclusive
    val generatedDouble = leftLimit + Random().nextDouble() * (rightLimit - leftLimit)
    return generatedDouble
}

fun ClosedRange<Double>.randomBigDecimal(): BigDecimal = random().toBigDecimal()

inline fun <reified T : Enum<T>> randomEnumValue(): T = (enumValues<T>()).toList().shuffled().first()


fun randomString(prefix: String = "", postfix: String = ""): String = "$prefix${UUID.randomUUID()}$postfix"

inline fun <reified T : Enum<T>> randomEnumSet(itemsCount: IntRange): Set<T> {
    val out = mutableSetOf<T>()
    val theCount: Int = itemsCount.random()
    if (theCount < 1) {
        return out.toSet()
    }
    (0..theCount).forEach {
        out += (enumValues<T>()).toList().shuffled().first()
    }
    return out.toSet()
}

fun ClosedRange<Instant>.randomInstant(): Instant {
    val leftLimit = start.toEpochMilli()
    val rightLimit = endInclusive.toEpochMilli()
    val randomEpochMilli = (leftLimit .. rightLimit).random()
    return Instant.ofEpochMilli(randomEpochMilli)
}
