package com.albertattard.presentation.micronaut.greeting

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/greeting")
class GreetingController internal constructor(
    private var service: GreetingService
) {

    @Get("/", produces = [MediaType.APPLICATION_JSON])
    fun index(): Greeting {
        return service.greet()
    }
}
