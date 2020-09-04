package com.example.demo.api

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ApiController {

    @PostMapping("/foo")
    fun foo(
            @RequestBody input:Input
    ):Response {
        return Response(
                s=input.s,
                inlinedString = input.inlinedString
        )
    }

}

data class Input(val s:String, val inlinedString: InlinedString)
inline class InlinedString(val x:String)

data class Response(
        val s:String,
        val inlinedString:InlinedString
)
