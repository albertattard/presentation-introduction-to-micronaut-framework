package com.albertattard.presentation.contacts

import com.albertattard.presentation.contacts.DatabaseHelper.createContact
import java.util.UUID
import org.jetbrains.exposed.sql.Database
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class ContactsControllerTest {

    @Autowired
    lateinit var client: WebTestClient

    @Autowired
    lateinit var database: Database

    @Test
    fun `should return the location from where the contact can be retrieved`() {
        val create = CreateContact(name = "Albert Attard", email = "albertattard@gmail.com")

        client.post()
            .uri("/contacts/")
            .bodyValue(create)
            .exchange()
            .expectStatus().isCreated
            .expectHeader().value(HttpHeaders.LOCATION) {
                assertTrue(it.matches("/contacts/([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})".toRegex()))
            }
            .expectBody().isEmpty
    }

    @Test
    fun `should return not found if contact is not found`() {
        client.get()
            .uri("/contacts/${UUID.randomUUID()}")
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `should return the contact when found`() {
        val contact = createContact(database, CreateContact(name = "Albert Attard", email = "albertattard@gmail.com"))

        client.get()
            .uri("/contacts/${contact.id}")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody<Contact>()
            .consumeWith {
                assertEquals(contact, it.responseBody)
            }
    }

    @Test
    fun `should return the list of contacts`() {
        client.get()
            .uri("/contacts/")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(Array<Contact>::class.java)
    }
}
