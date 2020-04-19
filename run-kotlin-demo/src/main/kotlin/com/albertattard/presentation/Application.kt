package com.albertattard.presentation

import java.io.File
import java.net.ConnectException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandlers
import java.util.concurrent.TimeUnit
import org.slf4j.LoggerFactory

object Application {

    private val LOGGER = LoggerFactory.getLogger(Application::class.java)

    @JvmStatic
    fun main(args: Array<String>) {
        LOGGER.debug("Starting application")
        val process = ProcessBuilder()
            .inheritIO()
            .directory(File("."))
            .command("java", "-jar", "micronaut-kotlin-demo/build/libs/micronaut-kotlin-demo-1.0-all.jar")
            .start()

        val client: HttpClient = HttpClient.newHttpClient()
        val request: HttpRequest = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/contacts/"))
            .build()

        loop@ for (i in 1..25) {
            try {
                LOGGER.debug("Checking")
                val response: HttpResponse<String> = client.send(request, BodyHandlers.ofString())
                LOGGER.debug("Response {}", response.body())
                break@loop
            } catch (e: ConnectException) {
                LOGGER.warn("Failed to connect")
            }

            TimeUnit.MILLISECONDS.sleep(100)
        }

        LOGGER.debug("Stopping the application")
        process.destroy()
        if (!process.waitFor(1, TimeUnit.MINUTES)) {
            LOGGER.debug("Killing the application")
            process.destroyForcibly()
        }
    }
}
