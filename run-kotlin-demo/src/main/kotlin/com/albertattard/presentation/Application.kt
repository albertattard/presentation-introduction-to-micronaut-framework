package com.albertattard.presentation

import java.io.File
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

        val response: HttpResponse<String> = client.send(request, BodyHandlers.ofString())
        println(response.body())

        TimeUnit.SECONDS.sleep(5)
        process.destroy()
        process.exitValue()
    }
}
