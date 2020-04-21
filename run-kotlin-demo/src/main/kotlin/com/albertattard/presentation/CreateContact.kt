package com.albertattard.presentation

import java.nio.charset.StandardCharsets
import kotlin.random.Random

data class CreateContact(
    val name: String,
    val email: String
) {
    fun toJson(): String =
        """{"name":"$name","email":"$email"}"""

    companion object {

        fun random(): CreateContact {
            val name = names[Random.nextInt(names.size)]
            val surname = surnames[Random.nextInt(surnames.size)]
            val email = "$name.$surname@somewhere.there"

            return CreateContact(name = "$name $surname", email = email)
        }

        private val names: List<String> by lazy {
            readLinesFromResource("/names.txt")
        }

        private val surnames: List<String> by lazy {
            readLinesFromResource("/surnames.txt")
        }

        private fun readLinesFromResource(path: String): List<String> =
            CreateContact::class.java
                .getResourceAsStream(path)
                .readBytes()
                .toString(StandardCharsets.UTF_8)
                .split("\n".toRegex())
    }
}
