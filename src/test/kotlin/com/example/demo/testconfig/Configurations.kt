package com.example.demo.testconfig

import com.example.demo.testconfig.TestConfigurations.codeSourceResourcesLocation
import com.example.demo.testutils.resources.CodeSourceResources

object TestConfigurations {
    val codeSourceResourcesLocation: String = CodeSourceResources
            .fileLocationAsString()
            .let {
                CodeSourceResources.replaceLocationSuffix(
                        location = it,
                        oldSuffix = "/out/test/classes/",
                        newSuffix = "/src/test/resources",
                        oldSuffixRequired = true
                )
            }
}

enum class CodeSourceResourceBucket(val resourceName:String) {
    ROOT(
            resourceName = ""
    ),
    GOLDEN_TEST_DATA(
            resourceName = "golden-test-data"
    )

    ;

    val qualifiedName:String = when(resourceName.isEmpty()) {
        true -> resourceName
        false-> "/$resourceName"
    }

    val codeSourceLocation:String = "$codeSourceResourcesLocation$qualifiedName"
}
