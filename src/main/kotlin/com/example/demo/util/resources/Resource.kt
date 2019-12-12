package com.example.demo.util.resources

fun loadResource(resource: String): String =
        try {
            object {}.javaClass.getResource(resource)
                    .readText(Charsets.UTF_8)
        } catch (all: Exception) {
            throw RuntimeException(
                    "Failed to load resource! resource.name: $resource! reason: ${all.message}", all
            )
        }
