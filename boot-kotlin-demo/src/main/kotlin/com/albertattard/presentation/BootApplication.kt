package com.albertattard.presentation

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class BootApplication {

    @Bean
    fun runner() =
        CommandLineRunner {}
}

fun main(args: Array<String>) {
    runApplication<BootApplication>(*args)
}
