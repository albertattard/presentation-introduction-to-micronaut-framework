package com.albertattard.presentation.micronaut.greeting

import javax.inject.Singleton

@Singleton
class HelloGreetingService : GreetingService {

    override fun greet() =
        Greeting("Hello World")
}
