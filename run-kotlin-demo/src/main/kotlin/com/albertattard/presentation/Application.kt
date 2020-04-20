package com.albertattard.presentation

import java.io.File
import java.net.ConnectException
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis
import org.apache.http.HttpEntity
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.slf4j.LoggerFactory

object Application {

    private val LOGGER = LoggerFactory.getLogger(Application::class.java)

    private var application = ApplicationType.MICRONAUT

    @JvmStatic
    fun main(args: Array<String>) {
        while (true) {
            val next = determineNextOperation()
            next.run()
        }
    }

    @JvmStatic
    private fun determineNextOperation(): Operation {
        while (true) {
            display("--------------------------------------------------")
            display("Next Operation")
            display("--------------------------------------------------")
            for (option in Operation.values()) {
                display("[${option.flag}] ${option.caption}")
            }
            display("--------------------------------------------------")

            val input = readLine()
            val operation = input.let { input ->
                Operation.values()
                    .firstOrNull { input == it.flag }
            }
            if (operation != null) {
                return operation
            }

            display("Invalid input: '$input'")
        }
    }

    private enum class Operation(val caption: String, val flag: String) : Runnable {
        SET_APPLICATION("Set Application", "a") {
            override fun run() {
                while (true) {
                    display("--------------------------------------------------")
                    display("Select application")
                    display("--------------------------------------------------")
                    for (application in ApplicationType.values()) {
                        display("[${application.flag}] ${application.caption}")
                    }
                    display("[x] Keep current (${application.caption})")
                    display("--------------------------------------------------")

                    val input = readLine()
                    val selected = input.let { input ->
                        when (input) {
                            "x" -> application
                            else -> ApplicationType.values()
                                .firstOrNull { input == it.flag }
                        }
                    }

                    if (selected != null) {
                        application = selected
                        return
                    }

                    display("Invalid input: '$input'")
                }
            }
        },

        TIME_TO_FIRST_RESPONSE("Time to First Response", "f") {
            override fun run() {
                LOGGER.debug("Starting application {}", application)
                val process = startApplication()

                val timeToFirstResponse = try {
                    timeToFirstResponse()
                } finally {
                    LOGGER.debug("Stopping the application")
                    stopApplication(process)
                }

                if (timeToFirstResponse > 0) {
                    display("$application Application took $timeToFirstResponse milliseconds to reply")
                } else {
                    display("$application Application timed out")
                }
            }
        },

        EXIT("Exit", "x") {
            override fun run() {
                display("Bye bye")
                exitProcess(0)
            }
        };
    }

    private enum class ApplicationType(val caption: String, val flag: String, val path: String) {
        BOOT("Spring Boot", "b", "boot-kotlin-demo/build/libs/boot-kotlin-demo-1.0.0.jar"),
        MICRONAUT("Micronaut", "m", "micronaut-kotlin-demo/build/libs/micronaut-kotlin-demo-1.0-all.jar");

        override fun toString(): String {
            return caption
        }
    }

    @JvmStatic
    private fun startApplication(): Process =
        ProcessBuilder()
            .inheritIO()
            .directory(File("."))
            .command("java", "-jar", application.path)
            .start()

    @JvmStatic
    private fun stopApplication(process: Process) {
        process.destroy()

        /* Kill the process if it does not stop after a minute */
        if (!process.waitFor(1, TimeUnit.MINUTES)) {
            LOGGER.debug("Killing the application")
            process.destroyForcibly()
        }
    }

    @JvmStatic
    private fun timeToFirstResponse(): Long {
        val httpClient = HttpClients.createDefault()
        httpClient.use {
            val request = HttpGet("http://localhost:8080/contacts/")

            var failed = true
            val time = measureTimeMillis {
                loop@ for (i in 1..1000) {
                    try {
                        LOGGER.debug("Checking")

                        val response = httpClient.execute(request)
                        response.use {
                            val entity: HttpEntity? = response.entity
                            if (entity != null) {
                                val result = EntityUtils.toString(entity)
                                LOGGER.debug("Response {}", result)
                            }
                        }

                        failed = false
                        break@loop
                    } catch (e: ConnectException) {
                        LOGGER.warn("Failed to connect")
                    }

                    TimeUnit.MILLISECONDS.sleep(10)
                }
            }

            return if (failed) -1L else time
        }
    }

    @JvmStatic
    private fun display(message: String) {
        println(message)
    }
}
