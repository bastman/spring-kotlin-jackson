package com.example.demo.examples.sealed001

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import java.time.Instant
import java.util.*

interface IEvent {
    val eventId: UUID
    val eventTime: Instant
    val eventType: String
    val data: Any
}

// Tell Jackson to include a property called 'type', which determines what concrete class is represented by the JSON
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "eventType")
sealed class Event : IEvent {
    abstract override val eventId: UUID
    abstract override val eventTime: Instant
    abstract override val eventType: String
}

data class Simple(
        val eventId: UUID,
        val eventTime: Instant,
        val eventType: String,
        val data: Map<String, Any?>
)

@JsonTypeName(RequestA.TYPE)
data class RequestA(
        override val eventId: UUID,
        override val eventTime: Instant,
        override val data: DetailsA
) : Event() {
    override val eventType: String = TYPE

    data class DetailsA(val a: String)

    companion object {
        const val TYPE: String = "events:RequestA"
    }
}

@JsonTypeName(RequestB.TYPE)
data class RequestB(
        override val eventId: UUID,
        override val eventTime: Instant,
        override val data: DetailsB
) : Event() {
    override val eventType: String = TYPE

    data class DetailsB(val b: String)

    companion object {
        const val TYPE: String = "events:RequestB"
    }
}
