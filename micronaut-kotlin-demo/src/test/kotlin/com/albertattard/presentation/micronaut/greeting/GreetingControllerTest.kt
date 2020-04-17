package com.albertattard.presentation.micronaut.greeting

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.annotation.MicronautTest
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.kotlintest.MicronautKotlinTestExtension.getMock
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

@MicronautTest
class GreetingControllerTest(
    private val service: GreetingService,
    @Client("/greeting") private val client: RxHttpClient
) : StringSpec({
    "should return the greeting message returned by the greeting service" {
        val mock = getMock(service)

        val greeting = Greeting("Hello Micronaut World")
        every { mock.greet() } returns greeting

        val result = client.toBlocking().retrieve("/", Greeting::class.java)
        result shouldBe greeting

        verify(exactly = 1) { mock.greet() }

        /* TODO: check why this needs to be verified */
        verify(exactly = 2) { mock.hashCode() }
        verify(exactly = 1) { mock.toString() }
        confirmVerified(mock)
    }
}) {
    @MockBean(GreetingService::class)
    fun greetingService(): GreetingService {
        return mockk()
    }
}
