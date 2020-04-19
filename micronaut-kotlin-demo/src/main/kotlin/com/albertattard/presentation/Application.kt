package com.albertattard.presentation

import io.micronaut.runtime.Micronaut

object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
                .packages("com.albertattard.presentation")
                .mainClass(Application.javaClass)
                .start()
    }
}
