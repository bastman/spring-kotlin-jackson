package com.example.demo.api

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ApiController {

    @PostMapping("/foo")
    fun foo(
            @RequestBody payload:Payload
    ):Any {
        return mapOf(
                "data" to payload
        )
    }

}

data class Payload(val s:String)
