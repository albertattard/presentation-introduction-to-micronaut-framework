package com.albertattard.presentation

import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class CreateContactTest {

    @Test
    fun `should create a random contact`() {
        val a = CreateContact.random()
        val b = CreateContact.random()
        assertNotEquals(a, b)
    }
}
