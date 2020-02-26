package com.example.demo.examples.poly001

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.time.Instant
import java.util.*


interface IEvent {
    val eventId: UUID
    val eventTime: Instant
    val eventType: String
    val data: Any
}


// Note: this is not(!) added to the polymorphic type hierarchy
data class Unknown(
        override val eventId: UUID,
        override val eventTime: Instant,
        override val eventType: String,
        override val data: Any
) : IEvent

// The polymporhic hierarchy

// Tell Jackson to include a property called 'type', which determines what concrete class is represented by the JSON
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "eventType")
@JsonSubTypes(
        JsonSubTypes.Type(EventA::class, name = EventA.TYPE),
        JsonSubTypes.Type(EventB::class, name = EventB.TYPE)
)
interface IEventPolymorphic : IEvent

data class EventA(
        override val eventId: UUID,
        override val eventTime: Instant,
        override val data: DetailsA
) : IEventPolymorphic {
    override val eventType: String = TYPE

    data class DetailsA(val a: String)
    companion object {
        const val TYPE = "the-event-A"
    }
}

data class EventB(
        override val eventId: UUID,
        override val eventTime: Instant,

        override val data: Map<String, Any?>
) : IEventPolymorphic {
    override val eventType: String = TYPE

    data class DetailsB(val b: String)
    companion object {
        const val TYPE = "the-event-B"
    }
}
