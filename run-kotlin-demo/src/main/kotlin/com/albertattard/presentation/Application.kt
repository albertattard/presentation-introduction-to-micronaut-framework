package com.albertattard.presentation

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File
import java.net.ConnectException
import java.util.UUID
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread
import kotlin.random.Random
import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis
import org.apache.http.HttpHeaders
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
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
            val operation = input.let { i ->
                Operation.values()
                    .firstOrNull { i == it.flag }
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
                    val selected = input.let { i ->
                        when (i) {
                            "x" -> application
                            else -> ApplicationType.values()
                                .firstOrNull { i == it.flag }
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
                val process = startApplication()

                val timeToFirstResponse = try {
                    timeToFirstResponse()
                } finally {
                    stopApplication(process)
                }

                if (timeToFirstResponse > 0) {
                    display("$application Application took $timeToFirstResponse milliseconds to reply")
                } else {
                    display("$application Application timed out")
                }
            }
        },

        RUN_APPLICATION("Run application", "r") {
            override fun run() {
                val process = startApplication()
                display("Application is staring. Type x to stop application")
                display("The application can be accessed from: http://localhost:8080/contacts/")

                try {
                    while (true) {
                        val input = readLine()
                        if (input == "x") {
                            break
                        }

                        display("Invalid input '$input'")
                        display("Type x to stop application")
                    }
                } finally {
                    stopApplication(process)
                }
            }
        },

        STRESS_TEST_APPLICATION("Stress-test application", "s") {
            override fun run() {
                val process = startApplication()
                display("Application is staring. Type x to stop application")

                try {
                    val terminate = AtomicBoolean()

                    val thread = thread {
                        stressTestApplication { terminate.get() }
                    }

                    while (true) {
                        val input = readLine()
                        if (input == "x") {
                            terminate.set(true)
                            break
                        }

                        display("Invalid input '$input'")
                        display("Type x to stop application")
                    }

                    display("Waiting for the background thread to stop")
                    thread.join()
                } finally {
                    stopApplication(process)
                }
            }
        },

        EXIT("Exit", "x") {
            override fun run() {
                display("Bye bye")
                exitProcess(0)
            }
        }
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
    private fun timeToFirstResponse(): Long =
        HttpClients.createDefault().use {
            waitForFirstResponse(it)
        }

    @JvmStatic
    private fun stressTestApplication(termination: () -> Boolean) {
        HttpClients.createDefault().use { client ->
            display("Waiting for the $application application to start")
            waitForFirstResponse(client)

            val size = 2000
            display("Creating random $size contacts")

            val created = (1..size).map {
                create(client)
            }.toList()

            display("Retrieving contacts at random, until interrupted")
            while (!termination()) {
                val id = created[Random.nextInt(created.size)]
                val contact = read(client, id)
                display("Retrieved: $contact")
            }
        }
    }

    @JvmStatic
    private fun waitForFirstResponse(httpClient: CloseableHttpClient): Long {
        var failed = true
        val time = measureTimeMillis {
            loop@ for (i in 1..1000) {
                try {
                    list(httpClient)
                    failed = false
                    break@loop
                } catch (e: ConnectException) {
                    /* Ignore */
                }

                TimeUnit.MILLISECONDS.sleep(10)
            }
        }

        return if (failed) -1L else time
    }

    @JvmStatic
    private fun list(httpClient: CloseableHttpClient): List<Contact> {
        val request = HttpGet("http://localhost:8080/contacts/")
        val response = httpClient.execute(request)
        return response.use { r ->
            r.entity?.let { e ->
                val mapper = jacksonObjectMapper()
                mapper.readValue<List<Contact>>(EntityUtils.toString(e))
            } ?: throw RuntimeException("Missing Content")
        }
    }

    @JvmStatic
    private fun read(httpClient: CloseableHttpClient, id: UUID): Contact {
        val request = HttpGet("http://localhost:8080/contacts/$id")
        val response = httpClient.execute(request)
        return response.use { r ->
            r.entity?.let { e ->
                val mapper = jacksonObjectMapper()
                mapper.readValue<Contact>(EntityUtils.toString(e))
            } ?: throw RuntimeException("Missing Content")
        }
    }

    @JvmStatic
    private fun create(httpClient: CloseableHttpClient): UUID {
        val create = HttpPost("http://localhost:8080/contacts/")

        val contact = CreateContact.random()
        val json = contact.toJson()
        create.entity = StringEntity(json)
        create.setHeader("Accept", "application/json")
        create.setHeader("Content-type", "application/json")

        val response = httpClient.execute(create)
        return response.use {
            response.getHeaders(HttpHeaders.LOCATION)
                .firstOrNull()
                ?.value
                ?.substringAfter("/contacts/")
                ?.let { UUID.fromString(it) }
                ?: throw RuntimeException("Missing location")
        }
    }

    @JvmStatic
    private fun display(message: String) {
        println(message)
    }
}
