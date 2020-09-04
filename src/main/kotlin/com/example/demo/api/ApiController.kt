package com.example.demo.api

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class ApiController {

    @PostMapping("/foo")
    fun foo(
            @RequestBody input:Input
    ):Response {
        return Response(
                s=input.s,
                inlinedString = input.inlinedString,
                inlinedIds = input.inlinedIds
        )
    }

}

data class Input(val s:String, val inlinedString: InlinedString, val inlinedIds: InlinedIds)
inline class InlinedString(val x:String)
inline class InlinedIds(val value:Set<UUID>)

data class Response(
        val s:String,
        val inlinedString:InlinedString,
        val inlinedIds:InlinedIds
)
