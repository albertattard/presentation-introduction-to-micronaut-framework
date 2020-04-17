package com.albertattard.presentation.micronaut

import io.micronaut.runtime.Micronaut

object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
                .packages("com.albertattard.presentation.micronaut")
                .mainClass(Application.javaClass)
                .start()
    }
}
