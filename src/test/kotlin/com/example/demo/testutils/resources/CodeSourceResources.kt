package com.example.demo.testutils.resources

import java.io.File
import java.net.URI
import java.net.URL
import java.nio.charset.Charset

/*
    private fun resourceSinkFolderLocation(): String {
        val x = object {}.javaClass.protectionDomain.codeSource.location.file
                .removeSuffix("/out/test/classes/")

        return "$x/src/test/resources"
    }
 */

object CodeSourceResources {
    /**
     * URI / URL ...
     * returns e.g.: file:<PATH_TO_PROJECT>/out/test/classes/
     * NOTE: ends with "/"
     */
    fun locationURL(): URL = object {}.javaClass.protectionDomain.codeSource.location
    fun locationURI(): URI = locationURL().toURI()
    /**
     * File
     * returns e.g.: <PATH_TO_PROJECT>/out/test/classes/
     * NOTE: ends with "/"
     */
    fun fileLocationAsString(): String = locationURL().file

    fun replaceLocationSuffix(location: String, oldSuffix: String, newSuffix: String, oldSuffixRequired: Boolean): String {
        if ((oldSuffixRequired) && (!location.endsWith(oldSuffix))) {
            error(
                    "Can not replace oldSuffice with newSuffix in location string!" +
                            "reason: location must end with oldSuffix !" +
                            " oldSuffix (expected): $oldSuffix newSuffix: $newSuffix location: $location"
            )
        }
        return location
                .removeSuffix(oldSuffix)
                .let { "$it$newSuffix" }
    }

    fun writeTextFile(location: String, content: String, charset: Charset = Charsets.UTF_8): File = try {
        val file = File(location)
        file.writeText(content, charset)
        file
    } catch (all: Exception) {
        throw RuntimeException(
                "Failed to save text file! sink location: $location reason: ${all.message}", all
        )
    }

}

