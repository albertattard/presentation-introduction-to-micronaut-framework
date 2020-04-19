package com.albertattard.presentation

import java.io.File
import java.net.ConnectException
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis
import org.apache.http.client.fluent.Request
import org.slf4j.LoggerFactory

object Application {

    private val LOGGER = LoggerFactory.getLogger(Application::class.java)

    @JvmStatic
    fun main(args: Array<String>) {
        val application = when (val flag = args.firstOrNull() ?: "-m") {
            "-b" -> "boot-kotlin-demo/build/libs/boot-kotlin-demo-1.0.0.jar"
            "-m" -> "micronaut-kotlin-demo/build/libs/micronaut-kotlin-demo-1.0-all.jar"
            else -> {
                LOGGER.error("Unknown application flag '{}'", flag)
                exitProcess(-1)
            }
        }

        LOGGER.debug("Starting application")
        val process = ProcessBuilder()
            .inheritIO()
            .directory(File("."))
            .command("java", "-jar", application)
            .start()

        val timeToFirstResponse = measureTimeMillis {
            loop@ for (i in 1..25) {
                try {
                    LOGGER.debug("Checking")

                    val response = Request.Get("http://localhost:8080/contacts/")
                        .execute()
                        .returnContent()

                    LOGGER.debug("Response {}", response)
                    break@loop
                } catch (e: ConnectException) {
                    LOGGER.warn("Failed to connect")
                }

                TimeUnit.MILLISECONDS.sleep(50)
            }
        }
        LOGGER.debug("Application took {} milliseconds to reply ({})", timeToFirstResponse, application)

        LOGGER.debug("Stopping the application")
        process.destroy()
        if (!process.waitFor(1, TimeUnit.MINUTES)) {
            LOGGER.debug("Killing the application")
            process.destroyForcibly()
        }
    }
}
